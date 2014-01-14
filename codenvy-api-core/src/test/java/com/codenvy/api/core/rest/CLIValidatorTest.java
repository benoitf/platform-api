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

import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * Test CLI annotated classes with CLIValidator
 *
 * @author Eugene Voevodin
 */
public class CLIValidatorTest {

    @Test
    public void testSingleService() throws ApiException {
        Assert.assertTrue(CLIValidator.isValid(TestService.class));
    }

    @Test(expectedExceptions = ApiException.class)
    public void testServiceWithCLIMethodThatDoesNotAnnotatedWithPath() throws ApiException {
        CLIValidator.validate(TestService1.class);
    }

    @Test(expectedExceptions = ApiException.class)
    public void testServiceWithMethodThatAnnotatedWithSamePositionCLIArguments() throws ApiException {
        CLIValidator.validate(TestService2.class);
    }

    @Test(expectedExceptions = ApiException.class)
    public void testServiceWithMethodWhichParameterIsAnnotatedWithArgumentButItIsNotAnnotatedWithAnyJAXRSParameterAnnotation()
            throws ApiException {
        CLIValidator.validate(TestService3.class);
    }

    @Test(expectedExceptions = ApiException.class)
    public void testServiceWithFieldWhichAnnotatedWithOptionButItIsNotAnnotatedWithAnyJAXRSParameterAnnotation() throws ApiException {
        CLIValidator.validate(TestService4.class);
    }

    @Test
    public void testServicesInheritance() {
        try {
            CLIValidator.validate(AbstractHelpService.class);
            Assert.fail();
        } catch (ApiException e) {
            //should be exception
        }
        Assert.assertTrue(CLIValidator.isValid(HelpService.class));
    }


    @Path("test")
    @CLI("test_command")
    class TestService extends Service {

        @HeaderParam("fake")
        @Option("-f")
        public String fake;

        @QueryParam("number")
        @Argument(1)
        public Integer argument;

        @Path("method")
        @POST
        @Produces("text/plaint")
        @CLI("sub_command")
        public String method(@Required @Description("Sub command argument") @QueryParam("value") @Argument(1) String value) {
            return value;
        }
    }


    @Path("test")
    @CLI("test_command")
    class TestService1 extends Service {

        @Path("method")
        @POST
        @Produces("text/plaint")
        @CLI("sub_command")
        public String method(@Required @Description("Sub command argument") @QueryParam("value") @Argument(1) String value) {
            return value;
        }

        @GET
        @Produces("text/plaint")
        @CLI("other_sub_command")
        public String otherMethod(@Required @Description("Other sub command argument") @QueryParam("value") @Argument(1) String value) {
            return value;
        }

    }

    @Path("test")
    @CLI("test_command")
    class TestService2 extends Service {
        @Path("method")
        @POST
        @Produces("text/plaint")
        @CLI("sub_command")
        public String method(@Required @Description("Sub command argument") @QueryParam("value") @Argument(1) String value,
                             @PathParam("other_value") @Argument(1) String otherValue) {
            return value;
        }

    }

    @Path("test")
    @CLI("test_command")
    class TestService3 extends Service {
        @Path("method")
        @POST
        @Produces("text/plaint")
        @CLI("sub_command")
        public String method(@Required @Description("Sub command argument") @QueryParam("value") @Argument(1) String value,
                             @Argument(2) String otherValue) {
            return value;
        }

    }

    @Path("test")
    @CLI("test_command")
    class TestService4 extends Service {
        @Option(value = "-f", fullValue = "--fake")
        public String fake;
    }

    @CLI("command")
    abstract class AbstractHelpService extends Service {

        @CLI("help")
        public abstract String test();
    }

    @Path("help_service")
    class HelpService extends AbstractHelpService {

        @Path("help")
        @Produces("text/plaint")
        @Override
        public String test() {
            return "Help information";
        }
    }
}
