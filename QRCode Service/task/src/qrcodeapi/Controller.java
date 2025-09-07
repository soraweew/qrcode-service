package qrcodeapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class Controller {

    static final Set<String> imageTypes = Set.of("png", "jpeg", "gif");
    static final Map<String, MediaType> mediaTypes = Map.of(
            "png", MediaType.IMAGE_PNG,
            "jpeg", MediaType.IMAGE_JPEG,
            "gif", MediaType.IMAGE_GIF
    );
    static final Set<String> correctionLevels = Set.of("L", "M", "Q", "H");

    static class Error {

        @JsonProperty("error")
        private String errorMessage;

        public Error(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public String getHealth() {
        return "200 OK";
    }

    @GetMapping("/qrcode")
    public ResponseEntity<?> getImage(
            @RequestParam String contents,
            @RequestParam(defaultValue = "250") int size,
            @RequestParam(defaultValue = "png") String type,
            @RequestParam(defaultValue = "L") String correction) {

        if (contents == null || contents.isBlank()) {
            return new ResponseEntity<>(
                    new Error("Contents cannot be null or blank"),
                    HttpStatus.BAD_REQUEST);
        }

        if (size < 150 || size > 350) {
            return new ResponseEntity<>(
                    new Error("Image size must be between 150 and 350 pixels"),
                    HttpStatus.BAD_REQUEST);
        }

        if (!correctionLevels.contains(correction)) {
            return new ResponseEntity<>(
                    new Error("Permitted error correction levels are L, M, Q, H"),
                    HttpStatus.BAD_REQUEST);
        }

        if (!imageTypes.contains(type)) {
            return new ResponseEntity<>(
                    new Error("Only png, jpeg and gif image types are supported"),
                    HttpStatus.BAD_REQUEST);
        }

        QRCode qrCode = new QRCode(contents, size, size, correction);

        return ResponseEntity
                .ok()
                .contentType(mediaTypes.get(type))
                .body(qrCode.getQRCode());
    }
}
