package com.netflix.niws.client.http;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

@Produces({"application/xml"})
@Path("/test")
public class TestResource {
	
	static boolean lastCallChunked = false;
	
	@Path("/getObject")
	@GET
	public Response getObject() {
		TestObject obj = new TestObject();
		obj.name = "test";
		return Response.ok(obj).build();
	}
	
	@Path("/setObject")
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response setObject(TestObject obj) {
		return Response.ok(obj).build();
	}
	
    @POST
    @Path("/postStream")
    @Consumes( { MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_XML})
    public Response handlePost(final InputStream in, @HeaderParam("Transfer-Encoding") String transferEncoding) {
        if (transferEncoding != null) {
            lastCallChunked = "chunked".equals(transferEncoding);
        }
        try {
            byte[] bytes = IOUtils.toByteArray(in);
            String entity = new String(bytes);
            return Response.ok(entity).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }    
}
