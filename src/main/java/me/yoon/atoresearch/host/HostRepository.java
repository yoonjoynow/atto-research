package me.yoon.atoresearch.host;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host, Integer> {

    boolean existsHostByNameOrAddress(String name, String address);
}
