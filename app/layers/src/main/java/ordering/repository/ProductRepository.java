package ordering.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ordering.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	Optional<Product> findByName(String name);
}
