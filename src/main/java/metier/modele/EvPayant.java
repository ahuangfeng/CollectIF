
package metier.modele;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;

@Entity
public class EvPayant extends Evnmt implements Serializable 
{
    
    private int montantPaf;

    public EvPayant() {
    }

    public EvPayant(int montantPaf,Date date, String moment, boolean planifie)
    {
        super( date,  moment,  planifie);
        this.montantPaf = montantPaf;
    }

    public int getMontantPaf() {
        return montantPaf;
    }

    public void setMontantPaf(int montantPaf) {
        this.montantPaf = montantPaf;
    }
    
    
  
     @Override
    public String toString() 
    {
        return super.toString() + " EvPayant{" + "montantPaf=" + montantPaf + '}';
    }
}
