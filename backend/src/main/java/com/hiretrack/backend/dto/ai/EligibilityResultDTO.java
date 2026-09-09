package com.hiretrack.backend.dto.ai;

import lombok.Data;
import java.util.List;

@Data
public class EligibilityResultDTO {
    private boolean eligible;
    private double matchPercentage;
    private List<String> missingRequirements;
    private List<String> reasons;
}
