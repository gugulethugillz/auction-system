package zw.co.cassavasmartech.auctionsystem.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

//    void store(MultipartFile file);

    Path storeImage(MultipartFile file);

    Path storeVideo(MultipartFile file);

    Path storeResource(MultipartFile file);

    Path storeDocument(MultipartFile file);

    Path storeAudio(MultipartFile file);

    Path storePdfDocument(MultipartFile file);

    Stream<Path> loadAll();

    Resource loadAsResource(Path path);

    void deleteAll();

    void deleteFile(Path path);
    void deleteImageFile(String fileName);
    void deleteVideoFile(String fileName);
}
