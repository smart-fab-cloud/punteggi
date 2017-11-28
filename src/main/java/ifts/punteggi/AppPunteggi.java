package ifts.punteggi;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class AppPunteggi extends Application<PunteggiConfig> {
    
    public static void main(String[] args) throws Exception {
        new AppPunteggi().run(args);
    }
    
    @Override
    public void run(PunteggiConfig configuration, Environment environment) {
        // Registra il servizio "Punteggi"
        final Punteggi risorsaPunteggi = new Punteggi(
                configuration.getPunteggioIniziale()
        );
        environment.jersey().register(risorsaPunteggi);
        
        // Registra il servizio "Utenti"
        final Utenti risorsaUtenti = new Utenti();
        environment.jersey().register(risorsaUtenti);
    }
}

