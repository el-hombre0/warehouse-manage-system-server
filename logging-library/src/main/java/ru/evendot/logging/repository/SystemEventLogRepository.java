package ru.evendot.logging.repository;

import ru.evendot.logging.model.SystemEventLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SystemEventLogRepository extends JpaRepository<SystemEventLog, Long> {

    // ===== Базовые запросы =====

    Optional<SystemEventLog> findByEventId(String eventId);

    List<SystemEventLog> findByCorrelationId(String correlationId);

    List<SystemEventLog> findByTransactionId(String transactionId);

    // ===== Запросы по сущностям =====

    List<SystemEventLog> findByEntityTypeAndEntityId(String entityType, Long entityId);

    @Query("SELECT l FROM SystemEventLog l WHERE l.entityType = :entityType AND l.entityPublicId = :publicId")
    List<SystemEventLog> findByEntityTypeAndPublicId(@Param("entityType") String entityType,
                                                     @Param("publicId") String publicId);

    @Query("SELECT l FROM SystemEventLog l WHERE l.parentEntityType = :parentType AND l.parentEntityId = :parentId")
    List<SystemEventLog> findByParentEntity(@Param("parentType") String parentType,
                                            @Param("parentId") Long parentId);

    // ===== Запросы по пользователям =====

    Page<SystemEventLog> findByUserIdOrderByEventTimestampDesc(Long userId, Pageable pageable);

    @Query("SELECT l FROM SystemEventLog l WHERE l.userEmail = :email ORDER BY l.eventTimestamp DESC")
    List<SystemEventLog> findByUserEmail(@Param("email") String email);

    // ===== Запросы по времени =====

    List<SystemEventLog> findByEventTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT l FROM SystemEventLog l WHERE l.eventTimestamp >= :since ORDER BY l.eventTimestamp DESC")
    List<SystemEventLog> findRecentEvents(@Param("since") LocalDateTime since);

    // ===== Запросы по сервисам и типам =====

    Page<SystemEventLog> findByServiceNameAndEventType(String serviceName, String eventType, Pageable pageable);

    @Query("SELECT DISTINCT l.eventType FROM SystemEventLog l WHERE l.serviceName = :serviceName")
    List<String> findDistinctEventTypesByService(@Param("serviceName") String serviceName);

    // ===== Аналитические запросы =====

    @Query("SELECT COUNT(l) FROM SystemEventLog l WHERE l.serviceName = :serviceName AND l.status = 'SUCCESS' AND l.eventTimestamp >= :since")
    Long countSuccessfulOperations(@Param("serviceName") String serviceName,
                                   @Param("since") LocalDateTime since);

    @Query("SELECT AVG(l.executionTimeMs) FROM SystemEventLog l WHERE l.serviceName = :serviceName AND l.operation = :operation")
    Double getAverageExecutionTime(@Param("serviceName") String serviceName,
                                   @Param("operation") String operation);

    @Query("SELECT l.eventType, COUNT(l) FROM SystemEventLog l WHERE l.serviceName = :serviceName GROUP BY l.eventType")
    List<Object[]> getEventTypeStatistics(@Param("serviceName") String serviceName);

    // ===== Запросы ошибок =====

    @Query("SELECT l FROM SystemEventLog l WHERE l.status = 'FAILURE' AND l.eventTimestamp >= :since ORDER BY l.eventTimestamp DESC")
    Page<SystemEventLog> findFailuresSince(@Param("since") LocalDateTime since, Pageable pageable);

    @Query("SELECT l.errorCode, COUNT(l) FROM SystemEventLog l WHERE l.serviceName = :serviceName AND l.status = 'FAILURE' GROUP BY l.errorCode")
    List<Object[]> getErrorStatistics(@Param("serviceName") String serviceName);

    // ===== Аудит изменений =====

    @Query("SELECT l FROM SystemEventLog l WHERE l.entityType = :entityType AND l.entityId = :entityId AND l.operation IN ('CREATE', 'UPDATE', 'DELETE')")
    List<SystemEventLog> getAuditTrail(@Param("entityType") String entityType,
                                       @Param("entityId") Long entityId);

    // ===== Очистка старых логов =====

    @Modifying
    @Transactional
    @Query("DELETE FROM SystemEventLog l WHERE l.eventTimestamp < :olderThan")
    int deleteOldLogs(@Param("olderThan") LocalDateTime olderThan);
}
