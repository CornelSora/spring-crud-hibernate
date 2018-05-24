package files.repositories;

import files.entities.File;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRepository extends PagingAndSortingRepository<File, Long> {
    Optional<File> findByFileName(@Param("fileName") String fileName);
}
