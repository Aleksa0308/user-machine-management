package com.raf.usermanagement.responses;

import com.raf.usermanagement.model.User;
import com.raf.usermanagement.model.Vacuum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
@Data
@Getter
@Setter
public class VacuumsResponse {
    private Collection<Vacuum> vacuums;
}
