/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Model.Documento;
import Model.Termo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andressa
 */

public class ResultadoBusca implements Comparable<ResultadoBusca>{

    private Documento documento;
    private double score;

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Documento getDocumento() {
        return documento;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
    
    @Override
    public int compareTo(ResultadoBusca outroDoc) {
     if (this.score > outroDoc.getScore()) {
          return -1;
     }
     if (this.score > outroDoc.getScore()) {
          return +1;
     }
     return 0;
    }
}
