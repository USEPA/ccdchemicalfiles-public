package gov.epa.ccte.api.chemicalfiles.repository;

import gov.epa.ccte.api.chemicalfiles.domain.ChemicalFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@SuppressWarnings("unused")
@RepositoryRestResource(exported = false)
public interface ChemicalFilesRepository extends JpaRepository<ChemicalFiles, String> {

    ChemicalFiles findByDtxsid(String dtxsid);

    List<ChemicalFiles> findByDtxsidIn(List<String> dtxsids);

    @Query(value = "select c.molImage from ChemicalFiles c where c.dtxsid = :dtxsid")
    byte[] getMolImageForDtxsid(@Param("dtxsid") String dtxsid);

    @Query(value = "select c.molImage from ChemicalFiles c where c.dtxcid = :dtxcid")
    byte[] getMolImageForDtxcid(@Param("dtxcid") String dtxcid);

    @Query(value = "select c.molFile from ChemicalFiles c where c.dtxsid = :dtxsid")
    String getMolFileForDtxsid(@Param("dtxsid") String dtxsid);

    @Query(value = "select c.molFile from ChemicalFiles c where c.dtxcid = :dtxcid")
    String getMolFileForDtxcid(@Param("dtxcid") String dtxcid);

}
