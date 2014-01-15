/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 *  [2012] - [2014] Codenvy, S.A. 
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
package com.codenvy.api.core.rest;

import com.codenvy.api.core.ApiException;
import com.codenvy.api.core.rest.annotations.Argument;
import com.codenvy.api.core.rest.annotations.CLI;
import com.codenvy.api.core.rest.annotations.Description;
import com.codenvy.api.core.rest.annotations.Option;
import com.codenvy.api.core.rest.annotations.Required;
import com.codenvy.api.core.rest.annotations.Valid;
import com.codenvy.api.core.rest.shared.JAXRSParameterType;
import com.codenvy.api.core.rest.shared.ParameterType;
import com.codenvy.api.core.rest.shared.dto.CLIArgument;
import com.codenvy.api.core.rest.shared.dto.CLIBase;
import com.codenvy.api.core.rest.shared.dto.CLICommand;
import com.codenvy.api.core.rest.shared.dto.CLIOption;
import com.codenvy.dto.server.DtoFactory;

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * CLI Mapped classes parser test.
 *
 * @author Eugene Voevodin
 */
public class CLIParserTest {

    private final CLIBase cliInherited;

    public CLIParserTest() throws ApiException {
        cliInherited = CLIParser.parse(ExtendedGunsService.class);
    }


    @Test
    public void testClassCommand() {
        Assert.assertEquals(cliInherited.getCommand(), "create");
        Assert.assertEquals(cliInherited.getDescription(), "Service for creating guns");
        Assert.assertEquals(cliInherited.getPath(), "create_gun");
    }

    @Test
    public void testClassArguments() throws ApiException, NoSuchMethodException {
        Assert.assertEquals(cliInherited.getArguments().size(), 1);
        Assert.assertEquals(toSet(createDto(CLIArgument.class).withPosition(1)
                                          .withParameterType(JAXRSParameterType.PATH)
                                          .withParameterName("pistol").withType(ParameterType.String)
                                          .withValid(Arrays.asList("usp", "glock"))), toSet(cliInherited.getArguments()));
    }

    @Test
    public void testClassOptions() {
        Assert.assertEquals(toSet(createDto(CLIOption.class).withValue("-f").withFullValue("--fake").withPair(true)
                                          .withParameterType(JAXRSParameterType.PATH).withParameterName("is_fake_gun")
                                          .withType(ParameterType.Boolean),
                                  createDto(CLIOption.class).withValue("-C").withFullValue("").withParameterType(JAXRSParameterType.QUERY)
                                          .withParameterName("gun_color").withType(ParameterType.String)),
                            toSet(cliInherited.getOptions()));
    }

    @Test
    public void testClassCommands() {
        Assert.assertEquals(cliInherited.getCommands().size(), 1);
        CLICommand current = cliInherited.getCommands().get(0);
        CLIArgument firstArgument =
                (CLIArgument)createDto(CLIArgument.class).withPosition(1).withType(ParameterType.String).withParameterType(
                        JAXRSParameterType.QUERY)
                        .withParameterName("gun_type").withRequired(true).withValid(Arrays.asList("m4a1", "ak47"))
                        .withDescription("Gun type");
        CLIArgument secondArgument =
                (CLIArgument)createDto(CLIArgument.class).withPosition(2).withType(ParameterType.Number).withParameterType(
                        JAXRSParameterType.QUERY)
                        .withParameterName("number_of_guns");
        Assert.assertEquals("lightweight_path", current.getPath());
        Assert.assertEquals("lightweight", current.getCommand());
        Assert.assertEquals("GET", current.getMethod());
        Assert.assertEquals(toSet("text/plaint"), toSet(current.getProduces()));
        Assert.assertEquals(toSet(firstArgument, secondArgument), toSet(current.getArguments()));
    }

    private <T> Set<T> toSet(T... values) {
        return new HashSet<>(Arrays.asList(values));
    }

    private <T> Set<T> toSet(List<T> list) {
        return new HashSet<>(list);
    }

    private <T> T createDto(Class<T> clazz) {
        return DtoFactory.getInstance().createDto(clazz);
    }

    @Path("create_gun")
    @CLI("create")
    @Description("Service for creating guns")
    class GunsService extends Service {

        @PathParam("is_fake_gun")
        @Option(value = "-f", fullValue = "--fake", isPair = true)
        public Boolean isFakeGun;

        @Path("lightweight_path")
        @GET
        @Produces("text/plaint")
        @CLI("lightweight")
        public String lightweightGut(
                @Required @Description("Gun type") @Valid({"m4a1", "ak47"}) @QueryParam("gun_type") @Argument(1) String test,
                @QueryParam("number_of_guns") @Argument(2) Integer guns) {
            return test;
        }
    }

    class ExtendedGunsService extends GunsService {

        @PathParam("pistol")
        @Valid({"usp", "glock"})
        @Argument(1)
        public String pistol;

        @QueryParam("gun_color")
        @Option("-C")
        public String gunColor;
    }
}