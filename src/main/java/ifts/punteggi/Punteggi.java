package ifts.punteggi;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

@Path("/punteggi")
@Produces(MediaType.APPLICATION_JSON)
public class Punteggi {
    
    private final int punteggioIniziale;
    private final List<Punteggio> punteggi;
    
    public Punteggi(int punteggioIniziale) {
        this.punteggioIniziale = punteggioIniziale;
        this.punteggi = new ArrayList<Punteggio>();
    }
    
    @POST
    public Response postPunteggio(
            @QueryParam("giocatore") Optional<String> giocatore, 
            @QueryParam("punteggio") Optional<Integer> punteggio
    ) {
        // Se giocatore non è stato specificato
        if(!giocatore.isPresent())
            // Restituisce un opportuno messaggio di errore
            return Response.status(Status.BAD_REQUEST)
                    .entity("Il nome del giocatore non può essere omesso")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        
        // Se esiste un giocatore "omonimo" con lo stesso nome
        if (indicePunteggio(giocatore.get())!=-1)
            // Restituisce un opportuno messaggio di errore
            return Response.status(Status.CONFLICT)
                    .entity("La risorsa " + giocatore.get() + " è già stata creata")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        
        // Crea un nuovo oggetto di tipo Punteggio
        // usando "punteggio", se presente (altrimenti "punteggioIniziale"
        Punteggio p;
        if (punteggio.isPresent())
            p = new Punteggio(giocatore.get(),punteggio.get());
        else
            p = new Punteggio(giocatore.get(),punteggioIniziale);
        
        // Aggiunge il punteggio creato alla collezione
        punteggi.add(p);
        
        // Crea la URI corrispondente al "giocatore" aggiunto 
        URI pUri = UriBuilder.fromResource(Punteggi.class)
                        .path(giocatore.get())
                        .build();
        return Response.created(pUri).build();
    }
    
    // Metodo privato usato per trovare l'indice del punteggio relativo ad
    // un "giocatore" nella collezione di "punteggi"
    private int indicePunteggio(String giocatore) {
        for(int i=0; i<punteggi.size(); i++)
            if(punteggi.get(i).getGiocatore().equals(giocatore))
                return i;
        return -1;
    }

    @GET
    @Path("/{giocatore}")
    public Response getPunteggio(@PathParam("giocatore") String giocatore) {
        // Cerca l'indice del punteggio relativo a "giocatore" e lo 
        // restituisce, se c'è
        int i = indicePunteggio(giocatore);
        if (i != -1)
            return Response.ok()
                    .entity(punteggi.get(i))
                    .build();
        // Altrimenti restituisce un messaggio di errore opportuno
        return Response.status(Status.NOT_FOUND)
                    .entity("La risorsa richiesta non esiste")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
    }
    
    
    @PUT
    @Path("/{giocatore}")
    public Response putPunteggio(@PathParam("giocatore") String giocatore, 
            @QueryParam("punteggio") Optional<Integer> punteggio) {
        // Se il punteggio non è indicato
        if (!punteggio.isPresent())
            // Restituisce un messaggio opportuno
            return Response.status(Status.BAD_REQUEST)
                    .entity("Il punteggio non può essere omesso")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        
        // Se il punteggio non è valido (valore negativo)
        if (punteggio.get() < 0)
            // Restituisce un messaggio opportuno
            return Response.status(Status.BAD_REQUEST)
                    .entity("Il punteggio non può essere negativo")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        
        // Cerca l'indice del punteggio relativo a "giocatore" 
        int i = indicePunteggio(giocatore);
        
        // Se c'è, lo rimuove da "punteggi" e ci inserisce un nuovo oggetto
        // che rappresenta il punteggio aggiornato
        if(i != -1) {
            punteggi.remove(i);
            punteggi.add(new Punteggio(giocatore,punteggio.get()));
            // Restituisce 200 Ok
            return Response.ok()
                    .entity("Risorsa aggiornata")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Altrimenti restituisce un messaggio di errore opportuno
        return Response.status(Status.NOT_FOUND)
                .entity("La risorsa richiesta non esiste")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
    
    @DELETE
    @Path("/{giocatore}")
    public Response deletePunteggio(@PathParam("giocatore") String giocatore) {     
        // Cerca l'indice del punteggio relativo a "giocatore" 
        int i = indicePunteggio(giocatore);
        // Se c'è, lo rimuove da "punteggi"
        if(i != -1) {
            punteggi.remove(i);
            // Restituisce 200 Ok
            return Response.ok()
                    .entity("Risorsa eliminata")
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
        
        // Altrimenti restituisce un messaggio di errore opportuno
        return Response.status(Status.NOT_FOUND)
                .entity("La risorsa richiesta non esiste")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }   
}


