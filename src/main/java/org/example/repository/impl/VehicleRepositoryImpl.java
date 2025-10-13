package org.example.repository.impl;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.example.models.core.Vehicle;
import org.example.repository.IRepository.IVehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class VehicleRepositoryImpl implements IVehicleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<Vehicle> vehicleRowMapper = (rs, rowNum) -> {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(rs.getLong("id"));
        vehicle.setVin(rs.getString("vin"));
        vehicle.setOemId(rs.getLong("oem_id"));
        vehicle.setModel(rs.getString("model"));
        vehicle.setModelYear(rs.getInt("model_year"));
        vehicle.setCustomerId(rs.getLong("customer_id"));
        if (rs.wasNull()) {
            vehicle.setCustomerId(null);
        }
        vehicle.setVehicle_data(rs.getString("vehicle_data"));
        vehicle.setWarranty_info(rs.getString("warranty_info"));
        return vehicle;
    };

    @Override
    public Vehicle save(Vehicle vehicle) {
        String sql = "INSERT INTO aoem.vehicles (vin, oem_id, model, model_year, customer_id, vehicle_data, warranty_info) VALUES (?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, vehicle.getVin());
            ps.setLong(2, vehicle.getOemId());
            ps.setString(3, vehicle.getModel());
            ps.setInt(4, vehicle.getModelYear());
            if (vehicle.getCustomerId() != null) {
                ps.setLong(5, vehicle.getCustomerId());
            } else {
                ps.setNull(5, java.sql.Types.BIGINT);
            }
            ps.setString(6, vehicle.getVehicle_data());
            ps.setString(7, vehicle.getWarranty_info());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            vehicle.setId(key.longValue());
        }
        return vehicle;
    }

    @Override
    public Optional<Vehicle> findById(Long id) {
        String sql = "SELECT * FROM aoem.vehicles WHERE id = ?";
        List<Vehicle> vehicles = jdbcTemplate.query(sql, vehicleRowMapper, id);
        return vehicles.isEmpty() ? Optional.empty() : Optional.of(vehicles.get(0));
    }

    @Override
    public List<Vehicle> findAll() {
        String sql = "SELECT * FROM aoem.vehicles";
        return jdbcTemplate.query(sql, vehicleRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM aoem.vehicles WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM aoem.vehicles WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public Optional<Vehicle> findByVin(String vin) {
        String sql = "SELECT * FROM aoem.vehicles WHERE vin = ?";
        List<Vehicle> vehicles = jdbcTemplate.query(sql, vehicleRowMapper, vin);
        return vehicles.isEmpty() ? Optional.empty() : Optional.of(vehicles.get(0));
    }

    @Override
    public boolean existsByVin(String vin) {
        String sql = "SELECT COUNT(*) FROM aoem.vehicles WHERE vin = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, vin);
        return count != null && count > 0;
    }

    @Override
    public List<Vehicle> findByVinIn(List<String> vins) {
        String sql = "SELECT * FROM aoem.vehicles WHERE vin IN ("
                + String.join(",", vins.stream().map(v -> "?").toArray(String[]::new)) + ")";
        return jdbcTemplate.query(sql, vehicleRowMapper, vins.toArray());
    }

    @Override
    public List<Vehicle> findByUserId(Long userId) {
        // Note: User relationship is through customer_id, not user_id in optimized
        // schema
        String sql = "SELECT * FROM aoem.vehicles WHERE customer_id = ?";
        return jdbcTemplate.query(sql, vehicleRowMapper, userId);
    }

    @Override
    public Long countByUserId(Long userId) {
        String sql = "SELECT COUNT(*) FROM aoem.vehicles WHERE customer_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, userId);
    }

    @Override
    public List<Vehicle> findByModel(String model) {
        String sql = "SELECT * FROM aoem.vehicles WHERE model = ?";
        return jdbcTemplate.query(sql, vehicleRowMapper, model);
    }

    @Override
    public List<Vehicle> findByModelAndVariant(String model, String variant) {
        String sql = "SELECT * FROM aoem.vehicles WHERE model = ? AND (vehicle_data::json->>'variant') = ?";
        return jdbcTemplate.query(sql, vehicleRowMapper, model, variant);
    }

    @Override
    public List<Vehicle> findByModelYear(Integer modelYear) {
        String sql = "SELECT * FROM vehicles WHERE model_year = ?";
        return jdbcTemplate.query(sql, vehicleRowMapper, modelYear);
    }

    @Override
    public List<Vehicle> findByWarrantyEndDateBefore(LocalDate date) {
        String sql = "SELECT * FROM aoem.vehicles WHERE (warranty_info::json->>'end_date')::date < ?";
        return jdbcTemplate.query(sql, vehicleRowMapper, java.sql.Date.valueOf(date));
    }

    @Override
    public List<Vehicle> findByWarrantyEndDateAfter(LocalDate date) {
        String sql = "SELECT * FROM aoem.vehicles WHERE (warranty_info::json->>'end_date')::date > ?";
        return jdbcTemplate.query(sql, vehicleRowMapper, java.sql.Date.valueOf(date));
    }

    @Override
    public List<Vehicle> findByWarrantyEndDateBetween(LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT * FROM aoem.vehicles WHERE (warranty_info::json->>'end_date')::date BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, vehicleRowMapper, java.sql.Date.valueOf(startDate),
                java.sql.Date.valueOf(endDate));
    }

    @Override
    public List<Vehicle> findActiveWarrantyVehicles() {
        String sql = "SELECT * FROM aoem.vehicles WHERE (warranty_info::json->>'end_date')::date > CURRENT_DATE";
        return jdbcTemplate.query(sql, vehicleRowMapper);
    }

    @Override
    public List<Vehicle> findExpiredWarrantyVehicles() {
        String sql = "SELECT * FROM aoem.vehicles WHERE (warranty_info::json->>'end_date')::date < CURRENT_DATE";
        return jdbcTemplate.query(sql, vehicleRowMapper);
    }

    @Override
    public List<Vehicle> searchVehicles(String keyword) {
        String sql = "SELECT * FROM aoem.vehicles WHERE LOWER(vin) LIKE ? OR LOWER(model) LIKE ? OR LOWER(vehicle_data::json->>'variant') LIKE ?";
        String searchPattern = "%" + keyword.toLowerCase() + "%";
        return jdbcTemplate.query(sql, vehicleRowMapper, searchPattern, searchPattern, searchPattern);
    }

    @Override
    public List<Vehicle> findByModelContainingIgnoreCase(String model) {
        String sql = "SELECT * FROM aoem.vehicles WHERE LOWER(model) LIKE ?";
        String searchPattern = "%" + model.toLowerCase() + "%";
        return jdbcTemplate.query(sql, vehicleRowMapper, searchPattern);
    }

    @Override
    public Long countByModel(String model) {
        String sql = "SELECT COUNT(*) FROM aoem.vehicles WHERE model = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, model);
    }

    @Override
    public Long countByModelYear(Integer modelYear) {
        String sql = "SELECT COUNT(*) FROM aoem.vehicles WHERE model_year = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, modelYear);
    }

    @Override
    public Long countActiveWarranties() {
        String sql = "SELECT COUNT(*) FROM aoem.vehicles WHERE (warranty_info::json->>'end_date')::date > CURRENT_DATE";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public Long countExpiredWarranties() {
        String sql = "SELECT COUNT(*) FROM aoem.vehicles WHERE (warranty_info::json->>'end_date')::date < CURRENT_DATE";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    // Additional methods from interface
    @Override
    public List<Vehicle> findVehiclesWithExpiredWarrantyButActiveParts() {
        String sql = "SELECT * FROM aoem.vehicles WHERE 1=0"; // vehicle_components removed
        return jdbcTemplate.query(sql, vehicleRowMapper);
    }

    @Override
    public List<Vehicle> findByCoverageRuleInWarrantyInfo(String coverageRule, boolean isActive) {
        String sql = "SELECT * FROM aoem.vehicles WHERE (warranty_info ILIKE ?) AND ? = true";
        String searchPattern = "%" + coverageRule + "%";
        return jdbcTemplate.query(sql, vehicleRowMapper, searchPattern, isActive);
    }

    @Override
    public List<Vehicle> findVehiclesWithActiveBatteryCoverage() {
        String sql = "SELECT * FROM aoem.vehicles WHERE (warranty_info::json->>'batteryCoverage')::boolean = true AND (warranty_info::json->>'end_date')::date > CURRENT_DATE";
        return jdbcTemplate.query(sql, vehicleRowMapper);
    }

    @Override
    public List<Vehicle> searchInAllJsonFields(String keyword) {
        String sql = "SELECT * FROM aoem.vehicles WHERE vehicle_data ILIKE ? OR warranty_info ILIKE ?";
        String like = "%" + keyword + "%";
        return jdbcTemplate.query(sql, vehicleRowMapper, like, like);
    }

    @Override
    public List<Vehicle> findByKmLimitInWarrantyInfo(Integer kmLimit) {
        String sql = "SELECT * FROM aoem.vehicles WHERE (warranty_info::json->>'km_limit')::int = ?";
        return jdbcTemplate.query(sql, vehicleRowMapper, kmLimit);
    }

    @Override
    public List<Vehicle> findByVariantInVehicleData(String variant) {
        String sql = "SELECT * FROM aoem.vehicles WHERE (vehicle_data::json->>'variant') = ?";
        return jdbcTemplate.query(sql, vehicleRowMapper, variant);
    }

    @Override
    public List<Vehicle> findByColorInVehicleData(String color) {
        String sql = "SELECT * FROM aoem.vehicles WHERE (vehicle_data::json->>'color') = ?";
        return jdbcTemplate.query(sql, vehicleRowMapper, color);
    }

    @Override
    public List<Vehicle> findByCustomerTypeInVehicleData(String customerType) {
        String sql = "SELECT * FROM aoem.vehicles WHERE (vehicle_data::json->>'customerType') = ?";
        return jdbcTemplate.query(sql, vehicleRowMapper, customerType);
    }

    @Override
    public List<Vehicle> findByBatteryCapacityInVehicleData(String batteryCapacity) {
        String sql = "SELECT * FROM aoem.vehicles WHERE (vehicle_data::json->>'batteryCapacity') = ?";
        return jdbcTemplate.query(sql, vehicleRowMapper, batteryCapacity);
    }

    @Override
    public List<Vehicle> findByWarrantyStartDateInWarrantyInfo(LocalDate startDate) {
        String sql = "SELECT * FROM aoem.vehicles WHERE (warranty_info::json->>'start_date')::date = ?";
        return jdbcTemplate.query(sql, vehicleRowMapper, java.sql.Date.valueOf(startDate));
    }
}