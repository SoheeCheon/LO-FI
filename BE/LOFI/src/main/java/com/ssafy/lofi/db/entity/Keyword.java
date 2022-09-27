package com.ssafy.lofi.db.entity;

import com.ssafy.lofi.dto.response.KeywordAPIResponse;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Keyword extends BaseEntity{

    // "id", "keyword", "animal_index_id", "person_index_id", "found_index_id", "lost_index_id"

    private String keyword;
    @ManyToOne
    @JoinColumn(name="id")
    private FoundArticle found_index_id;
    @ManyToOne
    @JoinColumn(name="id")
    private LostArticle lost_index_id;
    @ManyToOne
    @JoinColumn(name="id")
    private MissingAnimal animal_index_id;
    @ManyToOne
    @JoinColumn(name="id")
    private MissingPerson person_index_id;

}