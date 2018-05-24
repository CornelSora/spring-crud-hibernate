package files.controllers;

import files.entities.File;
import files.repositories.FileRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/files")
public class FileController {
    FileRepository repo;

    @Autowired
    public FileController(FileRepository repo) {
        this.repo = repo;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> postFile(@RequestBody File file){
        String jsonPrintString = "";
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(file.getXmlResult());
            jsonPrintString = xmlJSONObj.toString();
            // String xmlFromJson = XML.toString(xmlJSONObj);
            file.setJsonResult(jsonPrintString);
            Optional<File> fileBD = repo.findByFileName(file.getFileName());
            if (!fileBD.isPresent()) {
                repo.save(file);
            }
        } catch (JSONException je) {
            System.out.println(je.toString());
        }
        return ResponseEntity.ok(jsonPrintString);

    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<File> getFiles(){
        return repo.findAll();
    }

    @RequestMapping(value= "/{name}", method = RequestMethod.GET)
    public ResponseEntity<Optional<File>> getWidget(@PathVariable String name){
        Optional<File> file = repo.findByFileName(name);
        if (file.isPresent()){
            return ResponseEntity.ok(file);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/others", method = RequestMethod.POST)
    public ResponseEntity<String> postFileXMLUnknown(@RequestBody File file){
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(file.getXmlResult());
            String jsonPrintString = xmlJSONObj.toString();
            // String xmlFromJson = XML.toString(xmlJSONObj);
            file.setJsonResult(jsonPrintString);
            repo.save(file);
            return ResponseEntity.ok(jsonPrintString);
        } catch (JSONException je) {
            System.out.println(je.toString());
        }
        //  JSONObject xmlJSONObj = XML.toJSONObject(TEST_XML_STRING);
        //  String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
        return ResponseEntity.accepted().build();
    }

}
