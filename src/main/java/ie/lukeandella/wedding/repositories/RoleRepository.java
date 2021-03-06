package ie.lukeandella.wedding.repositories;

import ie.lukeandella.wedding.pojos.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query
    Role findByNameIs(String name);
}
