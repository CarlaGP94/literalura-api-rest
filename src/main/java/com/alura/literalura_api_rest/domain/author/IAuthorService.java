package com.alura.literalura_api_rest.domain.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAuthorService {

    Page<AuthorDetail> showAllAuthors(Pageable paginacion);

    Page<AuthorDetail> showBooksByLanguage(Pageable paginacion, String startYear, String endYear);
}
