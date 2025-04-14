package gov.epa.ccte.api.chemicalfiles.service;

import com.epam.indigo.Indigo;
import com.epam.indigo.IndigoObject;
import gov.epa.ccte.api.chemicalfiles.domain.ChemicalFiles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@SuppressWarnings("unused")
@Service
public class SdfService {

    private Indigo indigo = new Indigo();
    private IndigoObject indigoObject;

    public String convertToSdf(ChemicalFiles sdfFile){
        log.debug("single sdf file is generating");

        IndigoObject buf  = indigo.writeBuffer();

        IndigoObject sdf = getIndigoSdfObject(sdfFile, null);

        buf.sdfAppend(sdf);

        return buf.toString();
    }

    // multiple dtxsids
    public String convertToSdf(List<ChemicalFiles> sdfFiles, HashMap<String, Double> dtxsidAndSimilarity){
        log.debug("multiple sdf files are generating");

        IndigoObject buf  = indigo.writeBuffer();
        Double similarity = null;

        for(ChemicalFiles sdfFile : sdfFiles) {
            if(dtxsidAndSimilarity != null && dtxsidAndSimilarity.get(sdfFile.getDtxsid()) != null)
                similarity = dtxsidAndSimilarity.get(sdfFile.getDtxsid());
            else
                similarity = null;

            IndigoObject sdf = getIndigoSdfObject(sdfFile, similarity);
            buf.sdfAppend(sdf);
        }

        return buf.toString();
    }

    private IndigoObject getIndigoSdfObject(ChemicalFiles sdfFile, Double similarity) {
        log.debug("sdf object for dtxsid = {} ", sdfFile.getDtxsid());

        IndigoObject sdf;

        if(sdfFile.getMolFile() != null)
            sdf = indigo.loadMolecule(sdfFile.getMolFile());
        else
            sdf = indigo.createMolecule();

        if(sdfFile.getDtxsid() != null)
            sdf.setProperty("DTXSID", sdfFile.getDtxsid());
        else
            sdf.setProperty("DTXSID", "N/A");

        if(sdfFile.getPreferredName() != null)
            sdf.setProperty("PREFERRED_NAME", sdfFile.getPreferredName().toString());
        else
            sdf.setProperty("PREFERRED_NAME", "N/A");

        if(sdfFile.getCasrn() != null)
            sdf.setProperty("CASRN", sdfFile.getCasrn().toString());
        else
            sdf.setProperty("CASRN", "N/A");

        if(sdfFile.getInchikey() != null)
            sdf.setProperty("INCHIKEY", sdfFile.getInchikey().toString());
        else
            sdf.setProperty("INCHIKEY", "N/A");

        if(sdfFile.getIupacName() != null)
            sdf.setProperty("IUPAC_NAME", sdfFile.getIupacName().toString());
        else
            sdf.setProperty("IUPAC_NAME", "N/A");

        if(sdfFile.getSmiles() != null)
            sdf.setProperty("SMILES", sdfFile.getSmiles().toString());
        else
            sdf.setProperty("SMILES", "N/A");

        if(sdfFile.getInchiString() != null)
            sdf.setProperty("INCHI_STRING", sdfFile.getInchiString().toString());
        else
            sdf.setProperty("INCHI_STRING", "N/A");

        if(sdfFile.getMolFormula() != null)
            sdf.setProperty("MOLECULAR_FORMULA", sdfFile.getMolFormula().toString());
        else
            sdf.setProperty("MOLECULAR_FORMULA", "N/A");

        if(sdfFile.getAverageMass() != null)
            sdf.setProperty("AVERAGE_MASS", sdfFile.getAverageMass().toString());
        else
            sdf.setProperty("AVERAGE_MASS", "0");


        if(sdfFile.getAverageMass() != null)
            sdf.setProperty("AVERAGE_MASS", sdfFile.getAverageMass().toString());
        else
            sdf.setProperty("AVERAGE_MASS", "0");

        if(sdfFile.getMonoisotopicMass() != null)
            sdf.setProperty("MONOISOTOPIC_MASS", sdfFile.getMonoisotopicMass().toString());
        else
            sdf.setProperty("MONOISOTOPIC_MASS", "0");

        if(sdfFile.getSourcesCount() != null)
            sdf.setProperty("DATA_SOURCES", sdfFile.getSourcesCount().toString());
        else
            sdf.setProperty("DATA_SOURCES", "0");
        if(sdfFile.getPubmedCount() != null)
            sdf.setProperty("NUMBER_OF_PUBMED_ARTICLES", sdfFile.getPubmedCount().toString());
        else
            sdf.setProperty("NUMBER_OF_PUBMED_ARTICLES", "0");

        if(sdfFile.getPubchemCount() != null)
            sdf.setProperty("PUBCHEM_DATA_SOURCES", sdfFile.getPubchemCount().toString());
        else
            sdf.setProperty("PUBCHEM_DATA_SOURCES", "0");

        if(sdfFile.getCpdataCount() != null)
            sdf.setProperty("CPDAT_COUNT", sdfFile.getCpdataCount().toString());
        else
            sdf.setProperty("CPDAT_COUNT", "0");

        // if similarity is also pass to SDF
        if(similarity != null)
            sdf.setProperty("SIMILARITY", Double.toString(similarity));

        return sdf;
    }
}
