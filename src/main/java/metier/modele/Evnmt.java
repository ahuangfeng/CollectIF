package metier.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Evnmt implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date_evenement;
    private String moment;
    boolean planifie;

    @OneToMany
    private List<Demande> maListeDemandesEVT = new ArrayList();

    @ManyToOne //Clé étrangère
    @JoinColumn(name = "lieu_id")
    private Lieu monLieuMTO = null;

    public void addDemande(Demande maDem) {
        System.out.println("Ajout de " + maDem.toString() + " à " + this.toString());
        maListeDemandesEVT.add(maDem);
        System.out.println("Ajout effectué");
    }

    public boolean isPlanifie() {
        return planifie;
    }

    public void setPlanifie(boolean planifie) {
        this.planifie = planifie;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date_evenement;
    }

    public void setDate(Date date) {
        this.date_evenement = date;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public List<Demande> getMaListeDemandesEVT() {
        return maListeDemandesEVT;
    }

    public Lieu getMonLieuMTO() {
        return monLieuMTO;
    }

    public void setMonLieuMTO(Lieu monLieuMTO) {
        this.monLieuMTO = monLieuMTO;
    }

    protected Evnmt() {
    }

    public Evnmt(Date date, String moment, boolean planifie) {
        this.date_evenement = date;
        this.moment = moment;
        this.planifie = planifie;
    }

    public void setAssociatedLieu(Lieu monLieu) {
        monLieuMTO = monLieu;
        System.out.println("Ajout de " + monLieu.toString() + " à " + this.toString());
    }

    @Override
    public String toString() {
        return "Evnmt{" + "id=" + id + ", date=" + date_evenement + ", moment=" + moment + ", planifie="
                + planifie + '}';
    }
}
