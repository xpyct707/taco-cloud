package tacos.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import tacos.Taco;

@CrossOrigin(origins = "*")
public interface TacoRepository extends JpaRepository<Taco, Long> {
}
