package org.example.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.example.models.core.PartCatalog;
import org.example.models.enums.PartCategory;
import org.example.repository.IRepository.IPartCatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class PartCatalogRepositoryImpl implements IPartCatalogRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<PartCatalog> mapper = (rs, rowNum) -> {
        PartCatalog p = new PartCatalog();
        p.setId(rs.getLong("id"));
        p.setOemId(rs.getLong("oem_id"));
        p.setPartNumber(rs.getString("part_number"));
        p.setName(rs.getString("name"));
        p.setCategory(rs.getString("category"));
        p.setPart_data(rs.getString("part_data"));
        return p;
    };

    @Override
    public PartCatalog save(PartCatalog partCatalog) {
        if (partCatalog.getId() == null) {
            String sql = "INSERT INTO aoem.parts_catalog (oem_id, part_number, name, category, part_data) VALUES (?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(conn -> {
                var ps = conn.prepareStatement(sql, new String[] { "id" });
                ps.setLong(1, partCatalog.getOemId());
                ps.setString(2, partCatalog.getPartNumber());
                ps.setString(3, partCatalog.getName());
                ps.setString(4, partCatalog.getCategory());
                ps.setString(5, partCatalog.getPart_data());
                return ps;
            }, keyHolder);
            if (keyHolder.getKey() != null) {
                partCatalog.setId(keyHolder.getKey().longValue());
            }
            return partCatalog;
        } else {
            String sql = "UPDATE aoem.parts_catalog SET oem_id=?, part_number=?, name=?, category=?, part_data=? WHERE id=?";
            jdbcTemplate.update(sql,
                    partCatalog.getOemId(),
                    partCatalog.getPartNumber(),
                    partCatalog.getName(),
                    partCatalog.getCategory(),
                    partCatalog.getPart_data(),
                    partCatalog.getId());
            return partCatalog;
        }
    }

    @Override
    public Optional<PartCatalog> findById(Long id) {
        List<PartCatalog> list = jdbcTemplate.query("SELECT * FROM aoem.parts_catalog WHERE id=?", mapper, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public List<PartCatalog> findAll() {
        return jdbcTemplate.query("SELECT * FROM aoem.parts_catalog ORDER BY id DESC", mapper);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM aoem.parts_catalog WHERE id=?", id);
    }

    @Override
    public boolean existsById(Long id) {
        Long cnt = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM aoem.parts_catalog WHERE id=?", Long.class, id);
        return cnt != null && cnt > 0;
    }

    @Override
    public Optional<PartCatalog> findByPartNumber(String partNumber) {
        List<PartCatalog> list = jdbcTemplate.query("SELECT * FROM aoem.parts_catalog WHERE part_number=?", mapper,
                partNumber);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public boolean existsByPartNumber(String partNumber) {
        Long cnt = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM aoem.parts_catalog WHERE part_number=?",
                Long.class, partNumber);
        return cnt != null && cnt > 0;
    }

    @Override
    public List<PartCatalog> findByPartNumberIn(List<String> partNumbers) {
        if (partNumbers == null || partNumbers.isEmpty())
            return List.of();
        String placeholders = String.join(",", java.util.Collections.nCopies(partNumbers.size(), "?"));
        String sql = "SELECT * FROM aoem.parts_catalog WHERE part_number IN (" + placeholders + ")";
        return jdbcTemplate.query(sql, mapper, partNumbers.toArray());
    }

    @Override
    public List<PartCatalog> findByCategory(PartCategory category) {
        return jdbcTemplate.query("SELECT * FROM aoem.parts_catalog WHERE category=?", mapper, category.name());
    }

    @Override
    public Long countByCategory(PartCategory category) {
        Long cnt = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM aoem.parts_catalog WHERE category=?", Long.class,
                category.name());
        return cnt == null ? 0L : cnt;
    }

    @Override
    public List<PartCatalog> findByVehicleModel(String vehicleModel) {
        return new ArrayList<>();
    }

    @Override
    public List<PartCatalog> findByVehicleModelContainingIgnoreCase(String vehicleModel) {
        return new ArrayList<>();
    }

    @Override
    public List<PartCatalog> searchParts(String keyword) {
        String like = "%" + keyword + "%";
        String sql = "SELECT * FROM aoem.parts_catalog WHERE LOWER(part_number) LIKE LOWER(?) OR LOWER(name) LIKE LOWER(?)";
        return jdbcTemplate.query(sql, mapper, like, like);
    }

    @Override
    public List<PartCatalog> findByPartNameContainingIgnoreCase(String partName) {
        String like = "%" + partName + "%";
        return jdbcTemplate.query("SELECT * FROM aoem.parts_catalog WHERE LOWER(name) LIKE LOWER(?)", mapper, like);
    }

    @Override
    public List<PartCatalog> findByDescriptionContainingIgnoreCase(String description) {
        return new ArrayList<>();
    }

    @Override
    public List<PartCatalog> findByManufacturerContainingIgnoreCase(String manufacturer) {
        return new ArrayList<>();
    }

    @Override
    public List<PartCatalog> findByIsActive(Boolean isActive) {
        return findAll();
    }

    @Override
    public List<PartCatalog> findActiveParts() {
        return findAll();
    }

    @Override
    public List<PartCatalog> findByIsActiveAndCategory(Boolean isActive, PartCategory category) {
        return findByCategory(category);
    }

    @Override
    public List<PartCatalog> findByUnitCostBetween(Double minCost, Double maxCost) {
        return new ArrayList<>();
    }

    @Override
    public List<PartCatalog> findByUnitCostGreaterThan(Double cost) {
        return new ArrayList<>();
    }

    @Override
    public List<PartCatalog> findByUnitCostLessThan(Double cost) {
        return new ArrayList<>();
    }

    @Override
    public Long countByIsActive(Boolean isActive) {
        Long cnt = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM aoem.parts_catalog", Long.class);
        return cnt == null ? 0L : cnt;
    }

    @Override
    public Double averageUnitCostByCategory(PartCategory category) {
        return 0.0;
    }
}