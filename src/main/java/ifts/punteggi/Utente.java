package ifts.punteggi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Utente {

    private String nickname;
    private String cognome;
    private String nome;
    private String email;    
    
    public Utente() { }
    
    public Utente(
        String nickname,
        String cognome, 
        String nome,
        String email
    ) {
        this.nickname = nickname;
        this.cognome = cognome;
        this.nome = nome;
        this.email = email;
    }
    
    @JsonProperty
    public String getNickname() { return this.nickname; } 
    
    @JsonProperty
    public String getCognome() { return this.cognome; }    
    
    @JsonProperty
    public String getNome() { return this.nome; }
    
    @JsonProperty
    public String getEmail() { return this.email; }
    
}

