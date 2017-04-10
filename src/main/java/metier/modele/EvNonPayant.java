
package metier.modele;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;


@Entity
public class EvNonPayant extends Evnmt implements Serializable {

    public EvNonPayant() {
    }

    public EvNonPayant(Date date, String moment, boolean planifie) {
        super(date, moment, planifie);
    }

    @Override
    public String toString() {
        return super.toString() + " EvNonPayant";
    }
}
