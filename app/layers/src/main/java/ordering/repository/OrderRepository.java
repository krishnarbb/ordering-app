package ordering.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ordering.domain.Orders;

public interface OrderRepository extends JpaRepository<Orders, Long> {
	Optional<Orders> findById(Long id);
}
