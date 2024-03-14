package ru.espada.avito.statistic;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "statistic", schema = "avito")
public class StatisticEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userGroupId;

    private Long locationId;
    private Long microCategoryId;
    private Long date; // в днях
    private Integer count;

    @Override
    public String toString() {
        return "StatisticEntity{" +
                "id=" + id +
                ", userGroupId=" + userGroupId +
                ", locationId=" + locationId +
                ", microCategoryId=" + microCategoryId +
                ", date=" + date +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.hashCode() != this.hashCode()) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        StatisticEntity other = (StatisticEntity) obj;
        return Objects.equals(userGroupId, other.userGroupId)
                && Objects.equals(locationId, other.locationId)
                && Objects.equals(microCategoryId, other.microCategoryId)
                && Objects.equals(date, other.date)
                && Objects.equals(count, other.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userGroupId, locationId, microCategoryId, date, count);
    }

}
