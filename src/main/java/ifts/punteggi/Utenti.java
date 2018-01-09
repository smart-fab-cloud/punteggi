package ifts.punteggi;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Path("/utenti")
@Produces(MediaType.APPLICATION_JSON)
public class Utenti {
    
    private final List<Utente> utenti;
    
    public Utenti() {
        utenti = new ArrayList<Utente>();
    }
    
    private int indiceUtente(String nickname) {
        for (int i = 0; i<utenti.size(); i++)
            if (utenti.get(i).getNickname().equals(nickname))
                return i;
        return -1;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postUtente(Utente u) {
        
        // Se nickname, nome, cognome o email sono vuoti
        if(u.getNickname().isEmpty() ||
                u.getNome().isEmpty() ||
                u.getCognome().isEmpty() ||
                u.getEmail().isEmpty())
            // Restituisce un opportuno messaggio di errore
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Nickname, nome, cognome e email dell'utente non possono essere vuoti.")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        
        // Se esiste un utente avente lo stesso nickname
        if (indiceUtente(u.getNickname()) > -1)
            // Restituisce un opportuno messaggio di errore
            return Response.status(Response.Status.CONFLICT)
                    .entity("Il nickname " + u.getNickname() + " non è disponibile.")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        
        // Altrimenti, aggiunge il nuovo utente
        utenti.add(u);
        // e restituisce la URI ad esso corrispondente 
        URI uUri = UriBuilder.fromResource(Utenti.class)
                        .path(u.getNickname())
                        .build();
        return Response.created(uUri).build();
    }
    
    @GET
    @Path("/{nickname}")
    public Response recuperaUtente(@PathParam("nickname") String nickname) {
        int i = indiceUtente(nickname);
        if (i == -1)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(nickname + " non trovato.")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        
        Utente u = utenti.get(i);
        
        return Response.ok()
                .entity(u)
                .build();
    }
    
    @PUT
    @Path("/{nickname}")
    public Response aggiornaPunteggio(
            @PathParam("nickname") String nickname,
            @QueryParam("cognome") Optional<String> cognome,
            @QueryParam("nome") Optional<String> nome,
            @QueryParam("email") Optional<String> email
    ) {
        // Se non è presente alcun query parameter 
        if(!cognome.isPresent() && 
                !nome.isPresent() &&
                !email.isPresent()) 
            // Restituisce un opportuno messaggio di errore
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Nome, cognome e email dell'utente non possono essere vuoti.")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        
        int i = indiceUtente(nickname);
        
        // Se il nickname non è presente
        if(i == -1)
            // Restituisce un opportuno messaggio di errore
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(nickname + " non trovato.")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        
        // Altrimenti, aggiorna i campi specificati
        Utente vecchio = utenti.get(i);
        utenti.remove(i);
        String cognomeNuovo = vecchio.getCognome();
        if(cognome.isPresent())
            cognomeNuovo = cognome.get();
        String nomeNuovo = vecchio.getNome();
        if(nome.isPresent())
            nomeNuovo = nome.get();
        String emailNuova = vecchio.getEmail();
        if(email.isPresent())
            emailNuova = email.get();
        Utente nuovo = new Utente(nickname, cognomeNuovo, nomeNuovo, emailNuova);
        utenti.add(nuovo);
        
        // e restituisce 200 OK
        return Response.ok()
                .entity(nuovo)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
    
    @DELETE
    @Path("/{nickname}")
    public Response eliminaPunteggio(@PathParam("nickname") String nickname) {
        
        int i = indiceUtente(nickname);
        
        // Se il nickname non è presente
        if(i == -1)
            // Restituisce un opportuno messaggio di errore
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(nickname + " non trovato.")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        
        // Altrimenti, rimuove l'utente corrispondente
        utenti.remove(i);
        // e restituisce 200 OK
        return Response.ok()
                .entity(nickname + " eliminato.")
                .build();
    }
    
}

