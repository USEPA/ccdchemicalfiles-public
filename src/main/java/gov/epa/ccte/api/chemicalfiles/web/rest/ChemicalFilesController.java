package gov.epa.ccte.api.chemicalfiles.web.rest;


import gov.epa.ccte.api.chemicalfiles.domain.ChemicalFiles;
import gov.epa.ccte.api.chemicalfiles.domain.SdfRequest;
import gov.epa.ccte.api.chemicalfiles.repository.ChemicalFilesRepository;
import gov.epa.ccte.api.chemicalfiles.service.ChemicalConverterService;
import gov.epa.ccte.api.chemicalfiles.service.SdfService;
import gov.epa.ccte.api.chemicalfiles.web.rest.errors.NoMolTypeFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.http.MediaType.IMAGE_PNG;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@Slf4j
@RestController
@CrossOrigin
public class ChemicalFilesController {

    private final ChemicalFilesRepository chemicalFilesRepository;
    private final ChemicalConverterService chemicalConverterService;
    private final SdfService sdfService;

    public ChemicalFilesController(ChemicalFilesRepository chemicalFilesRepository,
                                   ChemicalConverterService chemicalConverterService,
                                   SdfService sdfService) {
        this.chemicalFilesRepository = chemicalFilesRepository;
        this.chemicalConverterService = chemicalConverterService;
        this.sdfService = sdfService;
    }


    @RequestMapping(value = "/sdf/by-dtxsid/", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<String> getSdfForDtxsidInBulk(@RequestBody List<SdfRequest> sdfRequestList) throws IOException {

        String sdfContent="";
        HashMap<String, Double> dtxsidAndSimilarity = new HashMap<String, Double>();

        if(sdfRequestList != null && sdfRequestList.size() > 0){
            // collect dtxsid and similarity values
            for(SdfRequest sdfRequest : sdfRequestList){
                if(sdfRequest.getSimilarity() == null)
                    dtxsidAndSimilarity.put(sdfRequest.getDtxsid(), null);
                else
                    dtxsidAndSimilarity.put(sdfRequest.getDtxsid(), sdfRequest.getSimilarity());
            }

            List<String> dtxsidList = new ArrayList<>(dtxsidAndSimilarity.keySet());

            List<ChemicalFiles> chemicals = chemicalFilesRepository.findByDtxsidIn(dtxsidList);

            sdfContent = sdfService.convertToSdf(chemicals, dtxsidAndSimilarity);

        }

        return ResponseEntity.ok().contentType(TEXT_PLAIN).body(sdfContent);
    }

    @RequestMapping(value = "/sdf/by-dtxsid/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getSdfForDtxsid(@PathVariable("id") String id) throws IOException {

        log.debug("dtxsid = {}", id);

        ChemicalFiles chemical = chemicalFilesRepository.findByDtxsid(id);
        String sdfContent = sdfService.convertToSdf(chemical);

        return ResponseEntity.ok().contentType(TEXT_PLAIN).body(sdfContent);
    }

    @RequestMapping(value = "/image/by-dtxcid/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<byte[]> getImageForDtxcid(@PathVariable("id") String id) throws IOException {
        log.debug("dtxsid = {}", id);

        byte[] image = chemicalFilesRepository.getMolImageForDtxcid(id);

        return ResponseEntity.ok().contentType(IMAGE_PNG).body(image);
    }

    @RequestMapping(value = "/image/by-dtxsid/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<byte[]> getImageForDtxsid(@PathVariable("id") String id) throws IOException {
        log.debug("dtxsid = {}", id);

        byte[] image = chemicalFilesRepository.getMolImageForDtxsid(id);

        return ResponseEntity.ok().contentType(IMAGE_PNG).body(image);
    }

    @RequestMapping(value = "/mol/by-dtxsid/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getMolFileForDtxsid(@PathVariable("id") String id, @RequestParam(name = "type", required = false) String type) throws IOException {

        log.debug(" mol file for {} with type = {}", id, type);

        String molFile = chemicalFilesRepository.getMolFileForDtxsid(id);

        String fileName = id + ".mol";
        String fileContent;

        if(StringUtils.isEmpty(type)){
            fileContent = molFile;
        }else if (type.equalsIgnoreCase("v2000")){
            ChemicalConverterService.MolV3000 molV3000 = new ChemicalConverterService.MolV3000(molFile);
            ChemicalConverterService.MolV2000 molV2000 = chemicalConverterService.convert(molV3000);

            fileContent = molV2000.getContent();
        }else{
            throw new NoMolTypeFoundException(type);
        }

        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.set("Content-Disposition", "attachment; filename=" + fileName);

        return ResponseEntity.ok().contentType(TEXT_PLAIN).headers(responseHeaders).body(fileContent);
    }

    @RequestMapping(value = "/mol/by-dtxcid/{id}", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<String> getMolFileForDtxcid(@PathVariable("id") String id, @RequestParam(name = "type", required = false) String type) throws IOException {

        log.debug(" mol file for {} with type = {}", id, type);

        //ChemicalFiles chemicalFiles = chemicalFilesRepository.findByDtxcid(id);
        String molFile = chemicalFilesRepository.getMolFileForDtxcid(id);

        String fileName = id + ".mol";
        String fileContent;

        if(StringUtils.isEmpty(type)){
            fileContent = molFile;
        }else if (type.equalsIgnoreCase("v2000")){
            ChemicalConverterService.MolV3000 molV3000 = new ChemicalConverterService.MolV3000(molFile);
            ChemicalConverterService.MolV2000 molV2000 = chemicalConverterService.convert(molV3000);

            fileContent = molV2000.getContent();
        }else{
            throw new NoMolTypeFoundException(type);
        }

        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.set("Content-Disposition", "attachment; filename=" + fileName);

        return ResponseEntity.ok().contentType(TEXT_PLAIN).headers(responseHeaders).body(fileContent);
    }
}
