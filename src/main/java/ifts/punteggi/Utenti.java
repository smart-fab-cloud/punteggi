package ifts.punteggi;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/utenti")
public class Utenti {
    
    private final List<Utente> utenti;
    
    public Utenti() {
        utenti = new ArrayList<Utente>();
    }
    
    // Da implementare
    
    @GET
    @Path("/{nickname}")
    public Response recuperaUtente(@PathParam("nickname") String nickname) {
        // Da implementare
        return Response.ok()
                .entity(nickname)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
    
}

