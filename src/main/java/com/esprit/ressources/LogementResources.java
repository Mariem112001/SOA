package com.esprit.ressources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.esprit.business.LogementBusiness;
import com.esprit.entities.Logement;
import com.esprit.filtres.Secured;

import java.util.List;


@Path("logements")
public class LogementResources {
	public static LogementBusiness LB=new LogementBusiness();

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public Response addLogement(Logement L){
		if(LB.addLogement(L))
			return Response.status(Status.CREATED).entity("add success").build();
		return Response.status(Status.NOT_FOUND).entity("echec add").build();
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(){
		if(LB.getLogements()!=null)
			return Response.status(Status.OK).entity(LB.getLogements()).build();
		else
			return Response.status(Status.NOT_FOUND).entity("lise vide").build();
	}

	@GET
	@Path("noconflict")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findbydelegation(@QueryParam(value = "delegation") String d) {
		List<Logement> logements = LB.getLogementsByDeleguation(d);
		if (logements != null && !logements.isEmpty()) {
			return Response.status(Status.OK).entity(logements).build();
		} else {
			return Response.status(Status.NOT_FOUND).entity("delegation not found").build();
		}
	}
	@DELETE
	@Path("{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteLogement(@PathParam("id") int id) {
		if (LB.deleteLogement(id)) {
			return Response.status(Status.OK).entity("Logement supprimé avec succès").build();
		} else {
			return Response.status(Status.NOT_FOUND).entity("Logement non trouvé").build();
		}
	}

	@PUT
	@Path("{id}")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateLogement(@PathParam("id") int id, Logement updatedLogement) {
		if (LB.updateLogement(id, updatedLogement)) {
			return Response.status(Status.OK).entity("Logement mis à jour avec succès").build();
		} else {
			return Response.status(Status.NOT_FOUND).entity("Logement non trouvé").build();
		}
	}
	@GET
	@Path("/logements")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLogementByReference(@QueryParam("reference") int reference) {
		if (reference == 0) {
			return Response.status(Status.BAD_REQUEST).entity("Référence invalide").build();
		}

		Logement logement = LB.getLogementsByReference(reference);

		if (logement != null) {
			return Response.ok(logement).build();
		}
		return Response.status(Status.NOT_FOUND).entity("Logement non trouvé").build();
	}


	@Path("/logements2")


		@GET
		@Secured
		@Produces(MediaType.TEXT_PLAIN)
		public Response getSecureData() {
			return Response.ok("This is a secure endpoint!").build();
		}

}
