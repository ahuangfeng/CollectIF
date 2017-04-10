
package metier.modele;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.Version;


@Entity
public class Demande implements Serializable 
    {
    @Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    private String moment;
    private Boolean traiter;
    
    @ManyToOne //Clé étrangère
    @JoinColumn(name="adh_id")
     private Adherent monAdhMTO;
    
    @ManyToOne //Clé étrangère
    @JoinColumn(name="act_id")
     private Activite monActMTO;
    
    @Column(name ="VERSION", nullable=false)
    @Version
    private int version;
   
      public Long getId() {
        return id;
    }

    public Boolean getTraiter() {
        return traiter;
    }

    public void setTraiter(Boolean traiter) {
        this.traiter = traiter;
    }

    public Adherent getMonAdhMTO() {
        return monAdhMTO;
    }

    public void setMonAdhMTO(Adherent monAdhMTO) {
        this.monAdhMTO = monAdhMTO;
    }

    public Activite getMonActMTO() {
        return monActMTO;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }
    
    protected Demande() 
    {
        
    }
    
    public Demande(Date date, String moment) {
        this.date = date;
        this.moment = moment;
        traiter = false;
    }    
    
     @Override
    public String toString() 
    {
        return "Demande{" + "id=" + id + ", date=" + date + ", moment=" + moment +", traiter ="+traiter+ '}';
    }

    public void setAssociatedAdh(Adherent Adh) {
        
        monAdhMTO=Adh;
        System.out.println("Ajout de " + Adh.toString() + " à " + this.toString());   
    }
    
    public void setAssociatedAct(Activite Act) {   
        monActMTO=Act;
        System.out.println("Ajout de " + Act.toString() + " à " + this.toString()); 
    }
}
