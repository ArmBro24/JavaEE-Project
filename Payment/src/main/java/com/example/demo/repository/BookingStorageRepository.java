package com.example.demo.repository;


import com.example.demo.entity.BookingStorage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingStorageRepository extends JpaRepository<BookingStorage, Long> {
}
