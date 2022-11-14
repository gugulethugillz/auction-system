package zw.co.cassavasmartech.auctionsystem.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import zw.co.cassavasmartech.auctionsystem.common.Utils;
import zw.co.cassavasmartech.auctionsystem.common.AuctionSystemProperties;
import zw.co.cassavasmartech.auctionsystem.common.enums.ResourceType;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import static zw.co.cassavasmartech.auctionsystem.common.Utils.generateFileName;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileSystemStorageService implements StorageService {
    private final AuctionSystemProperties props;

//    @Override
    private Path store(MultipartFile file, Path location) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        filename = generateFileName(filename);
        Path path = null;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            path = location.resolve(filename);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return path;
    }

    @Override
    public Path storeImage(MultipartFile file) {
        return store(file, Paths.get(props.getImages()));
    }

    @Override
    public Path storeVideo(MultipartFile file) {
        return store(file, Paths.get(props.getVideos()));
    }

    @Override
    public Path storeResource(MultipartFile file) {
        ResourceType resourceType = Utils.guessResourceType(file);
        switch (resourceType) {
            case PDF_DOCUMENT:
                return store(file, Paths.get(props.getPdfDocuments()));
            case OTHER_DOCUMENT:
            default:
                return store(file, Paths.get(props.getMiscFiles()));
        }
    }

    @Override
    public Path storeDocument(MultipartFile file) {
        return store(file, Paths.get(props.getDocuments()));
    }

    @Override
    public Path storeAudio(MultipartFile file) {
        return store(file, Paths.get(props.getAudio()));
    }

    @Override
    public Path storePdfDocument(MultipartFile file) {
        return store(file, Paths.get(props.getPdfDocuments()));
    }

    @Override
    public void deleteImageFile(String fileName) {
        if(StringUtils.isEmpty(fileName)) return;
        log.info("File is {}", fileName);
        final Path path = Paths.get(props.getImages()).resolve(fileName);
        deleteFile(path);
    }

    @Override
    public void deleteVideoFile(String fileName) {
        if(StringUtils.isEmpty(fileName)) return;
        log.info("Deleting Video File {}", fileName);
        final Path path = Paths.get(props.getVideos()).resolve(fileName);
        deleteFile(path);
    }

    @Override
    public void deleteFile(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
            throw new StorageException("Failed to delete file", e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            final Path rootLocation = Paths.get(props.getFileBaseLocation());
            return Files.walk(rootLocation, 1)
                .filter(path -> !path.equals(rootLocation))
                .map(rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Resource loadAsResource(Path path) {
        try {
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + path);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + path, e);
        }
    }


    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(props.getFileBaseLocation()).toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(Paths.get(props.getImages()));
            Files.createDirectories(Paths.get(props.getAudio()));
            Files.createDirectories(Paths.get(props.getDocuments()));
            Files.createDirectories(Paths.get(props.getPdfDocuments()));
            Files.createDirectories(Paths.get(props.getVideos()));
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
