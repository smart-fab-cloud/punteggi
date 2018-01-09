package ifts.punteggi;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Path("/utenti")
public class Utenti {
    
    private final List<Utente> utenti;
    
    public Utenti() {
        utenti = new ArrayList<Utente>();
    }
    
    private int indiceUtente(Utente u) {
        for (int i = 0; i<utenti.size(); i++)
            if (utenti.get(i).getNickname().equals(u.getNickname()))
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
        if (indiceUtente(u) > -1)
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
        // Da implementare
        return Response.ok()
                .entity(nickname)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
    
    // Da implementare
    
}

