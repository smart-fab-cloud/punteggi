package ifts.punteggi;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class PunteggiConfig extends Configuration {
    
    private int punteggioIniziale; 
    
    @JsonProperty
    public int getPunteggioIniziale() {
        return punteggioIniziale;
    }
    
    @JsonProperty
    public void setPunteggioIniziale(int punteggioIniziale) {
        this.punteggioIniziale = punteggioIniziale;
    }
}
