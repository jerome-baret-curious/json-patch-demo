package dev.jerome.baret;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("tester")
public class DemoController {

    private final TesterRepository testerRepository;
    private final WorkRepository workRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    public DemoController(TesterRepository testerRepository, WorkRepository workRepository) {
        this.testerRepository = testerRepository;
        this.workRepository = workRepository;
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Tester readTester(@PathVariable Long id) {
        return testerRepository.findById(id).orElseThrow();
    }

    @PostMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Tester createTester(@RequestBody Tester body) {
        if (body.getWork() != null) {
            workRepository.save(body.getWork());
        }
        return testerRepository.save(body);
    }

    @PatchMapping(path = "/{id}/with-6902", consumes = "application/json-patch+json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Tester updateTester(@PathVariable Long id, @RequestBody JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Tester tester = testerRepository.findById(id).orElseThrow();
        Tester testerPatched = applyPatchToTester(patch, tester);
        return testerRepository.save(testerPatched);
    }

    private Tester applyPatchToTester(
            JsonPatch patch, Tester target) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(target, JsonNode.class));
        return objectMapper.treeToValue(patched, Tester.class);
    }


    @PatchMapping(path = "/{id}/with-7386", consumes = "application/merge-patch+json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Tester updateTester(@PathVariable Long id, @RequestBody JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        Tester tester = testerRepository.findById(id).orElseThrow();
        Tester testerPatched = applyMergePatchToTester(patch, tester);
        return testerRepository.save(testerPatched);
    }

    private Tester applyMergePatchToTester(
            JsonMergePatch patch, Tester target) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(target, JsonNode.class));
        return objectMapper.treeToValue(patched, Tester.class);
    }

}
