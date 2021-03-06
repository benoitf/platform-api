/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2013] Codenvy, S.A.
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.api.user.server;


import com.codenvy.api.core.rest.Service;
import com.codenvy.api.core.rest.annotations.Description;
import com.codenvy.api.core.rest.annotations.GenerateLink;
import com.codenvy.api.core.rest.annotations.Required;
import com.codenvy.api.core.rest.shared.ParameterType;
import com.codenvy.api.core.rest.shared.dto.Link;
import com.codenvy.api.core.rest.shared.dto.LinkParameter;
import com.codenvy.api.user.server.dao.UserDao;
import com.codenvy.api.user.server.dao.UserProfileDao;
import com.codenvy.api.user.server.exception.ProfileNotFoundException;
import com.codenvy.api.user.server.exception.UserException;
import com.codenvy.api.user.server.exception.UserNotFoundException;
import com.codenvy.api.user.server.exception.UserProfileException;
import com.codenvy.api.user.shared.dto.Attribute;
import com.codenvy.api.user.shared.dto.Profile;
import com.codenvy.api.user.shared.dto.User;
import com.codenvy.dto.server.DtoFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriBuilder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User profile API
 *
 * @author Eugene Voevodin
 * @author Max Shaposhnik
 */
@Path("profile")
public class UserProfileService extends Service {
    private final UserProfileDao profileDao;
    private final UserDao        userDao;

    @Inject
    public UserProfileService(UserProfileDao profileDao, UserDao userDao) {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GET
    @RolesAllowed("user")
    @GenerateLink(rel = "current profile")
    @Produces(MediaType.APPLICATION_JSON)
    public Profile getCurrent(@Context SecurityContext securityContext,
                              @Description("Preferences path filter") @QueryParam("filter") String filter)
            throws UserException, UserProfileException {
        final Principal principal = securityContext.getUserPrincipal();
        final User user = userDao.getByAlias(principal.getName());
        if (user == null) {
            throw new UserNotFoundException(principal.getName());
        }
        Profile profile;
        if (filter == null) {
            profile = profileDao.getById(user.getId());
        } else {
            profile = profileDao.getById(user.getId(), filter);
        }
        if (profile == null) {
            throw new ProfileNotFoundException(user.getId());
        }
        List<Attribute> attrs = new ArrayList<>();
        if (profile.getAttributes() != null) {
            attrs.addAll(profile.getAttributes());
        }
        attrs.add(DtoFactory.getInstance().createDto(Attribute.class)
                            .withDescription("User email")
                            .withName("email")
                            .withValue(user.getEmail()));
        profile.setAttributes(attrs);
        injectLinks(profile, securityContext);
        return profile;
    }

    @POST
    @RolesAllowed("user")
    @GenerateLink(rel = "update current")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Profile updateCurrent(@Context SecurityContext securityContext, @Description("updates for profile") List<Attribute> updates)
            throws UserException, UserProfileException {
        final Principal principal = securityContext.getUserPrincipal();
        final User user = userDao.getByAlias(principal.getName());
        if (user == null) {
            throw new UserNotFoundException(principal.getName());
        }
        Profile profile = profileDao.getById(user.getId());
        if (profile == null) {
            throw new ProfileNotFoundException(user.getId());
        }
        //if updates are not null, append it to existed attributes
        if (updates != null) {
            Map<String, Attribute> m = new LinkedHashMap<>(updates.size());
            for (Attribute attribute : updates) {
                m.put(attribute.getName(), attribute);
            }
            for (Iterator<Attribute> i = profile.getAttributes().iterator(); i.hasNext(); ) {
                Attribute attribute = i.next();
                if (m.containsKey(attribute.getName())) {
                    i.remove();
                }
            }
            profile.getAttributes().addAll(updates);
        } else {
            //if updates are null - clear profile attributes
            profile.setAttributes(new ArrayList<Attribute>());
        }
        profileDao.update(profile);
        injectLinks(profile, securityContext);
        return profile;
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"user", "system/admin", "system/manager"})
    @GenerateLink(rel = "get by id")
    @Produces(MediaType.APPLICATION_JSON)
    public Profile getById(@PathParam("id") String profileId, @Context SecurityContext securityContext)
            throws UserProfileException, UserException {
        final Profile profile = profileDao.getById(profileId);
        if (profile == null) {
            throw new ProfileNotFoundException(profileId);
        }
        final User user = userDao.getById(profile.getUserId());
        if (user == null) {
            throw new UserNotFoundException(profile.getUserId());
        }
        //prefs available only for CURRENT user profile
        profile.setPreferences(Collections.<String, String>emptyMap());
        List<Attribute> attrs = new ArrayList<>();
        if (profile.getAttributes() != null) {
            attrs.addAll(profile.getAttributes());
        }
        attrs.add(DtoFactory.getInstance().createDto(Attribute.class)
                            .withDescription("User email")
                            .withName("email")
                            .withValue(user.getEmail()));
        profile.setAttributes(attrs);
        injectLinks(profile, securityContext);
        return profile;
    }

    @POST
    @Path("{id}")
    @RolesAllowed({"system/admin", "system/manager"})
    @GenerateLink(rel = "update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Profile update(@PathParam("id") String profileId, @Required @Description("updates for profile") List<Attribute> updates,
                          @Context SecurityContext securityContext)
            throws UserProfileException {
        Profile profile = profileDao.getById(profileId);
        if (profile == null) {
            throw new ProfileNotFoundException(profileId);
        }
        //if updates are not null, append it to existed attributes
        if (updates != null) {
            Map<String, Attribute> m = new LinkedHashMap<>(updates.size());
            for (Attribute attribute : updates) {
                m.put(attribute.getName(), attribute);
            }
            for (Iterator<Attribute> i = profile.getAttributes().iterator(); i.hasNext(); ) {
                Attribute attribute = i.next();
                if (m.containsKey(attribute.getName())) {
                    i.remove();
                }
            }
            profile.getAttributes().addAll(updates);
        } else {
            //if updates are null - clear profile attributes
            profile.setAttributes(new ArrayList<Attribute>());
        }
        profileDao.update(profile);
        injectLinks(profile, securityContext);
        return profile;
    }

    @POST
    @Path("prefs")
    @RolesAllowed({"user"})
    @GenerateLink(rel = Constants.LINK_REL_UPDATE_PREFS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Profile updatePrefs(@Context SecurityContext securityContext,
                               @Description("preferences to update") Map<String, String> prefsToUpdate)
            throws UserException, UserProfileException {
        final Principal principal = securityContext.getUserPrincipal();
        final User current = userDao.getByAlias(principal.getName());
        if (current == null) {
            throw new UserNotFoundException(principal.getName());
        }
        final Profile currentProfile = profileDao.getById(current.getId());
        if (currentProfile == null) {
            throw new ProfileNotFoundException(current.getId());
        }
        //if given preferences are not null append it to existed preferences
        if (prefsToUpdate != null) {
            Map<String, String> currentPrefs = currentProfile.getPreferences();
            currentPrefs.putAll(prefsToUpdate);
            currentProfile.setPreferences(currentPrefs);
        } else {
            //if given preferences are null - clear profile preferences
            currentProfile.setPreferences(new HashMap<String, String>());
        }
        profileDao.update(currentProfile);
        injectLinks(currentProfile, securityContext);
        return currentProfile;
    }

    private void injectLinks(Profile profile, SecurityContext securityContext) {
        final List<Link> links = new ArrayList<>();
        final UriBuilder uriBuilder = getServiceContext().getServiceUriBuilder();
        if (securityContext.isUserInRole("user")) {
            links.add(createLink("GET", Constants.LINK_REL_GET_CURRENT_USER_PROFILE, null, MediaType.APPLICATION_JSON,
                                 uriBuilder.clone().path(getClass(), "getCurrent").build().toString()));
            links.add(createLink("POST", Constants.LINK_REL_UPDATE_CURRENT_USER_PROFILE, MediaType.APPLICATION_JSON,
                                 MediaType.APPLICATION_JSON,
                                 uriBuilder.clone().path(getClass(), "updateCurrent").build().toString())
                              .withParameters(Arrays.asList(DtoFactory.getInstance().createDto(LinkParameter.class)
                                                                      .withDescription("update profile")
                                                                      .withRequired(true)
                                                                      .withType(ParameterType.Object))));
            links.add(createLink("POST", Constants.LINK_REL_UPDATE_PREFS, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON,
                                 uriBuilder.clone().path(getClass(), "updatePrefs").build().toString()));
        }
        if (securityContext.isUserInRole("system/admin") || securityContext.isUserInRole("system/manager")) {
            links.add(createLink("GET", Constants.LINK_REL_GET_USER_PROFILE_BY_ID, null, MediaType.APPLICATION_JSON,
                                 uriBuilder.clone().path(getClass(), "getById").build(profile.getId()).toString()));
            links.add(createLink("POST", Constants.LINK_REL_UPDATE_USER_PROFILE_BY_ID, MediaType.APPLICATION_JSON,
                                 MediaType.APPLICATION_JSON,
                                 uriBuilder.clone().path(getClass(), "update").build(profile.getId()).toString())
                              .withParameters(Arrays.asList(DtoFactory.getInstance().createDto(LinkParameter.class)
                                                                      .withDescription("update profile")
                                                                      .withRequired(true)
                                                                      .withType(ParameterType.Object))));
        }
        profile.setLinks(links);
    }

    private Link createLink(String method, String rel, String consumes, String produces, String href) {
        return DtoFactory.getInstance().createDto(Link.class)
                         .withMethod(method)
                         .withRel(rel)
                         .withProduces(produces)
                         .withConsumes(consumes)
                         .withHref(href);
    }
}