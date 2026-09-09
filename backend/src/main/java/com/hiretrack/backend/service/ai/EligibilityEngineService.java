package com.hiretrack.backend.service.ai;

import com.hiretrack.backend.dto.ai.EligibilityResultDTO;
import com.hiretrack.backend.entity.Drive;
import com.hiretrack.backend.entity.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EligibilityEngineService {

    public EligibilityResultDTO checkEligibility(Student student, Drive drive) {
        EligibilityResultDTO result = new EligibilityResultDTO();
        List<String> missing = new ArrayList<>();
        List<String> reasons = new ArrayList<>();
        boolean eligible = true;

        if (student.getCgpa() != null && drive.getMinCgpa() != null) {
            if (student.getCgpa().compareTo(drive.getMinCgpa()) < 0) {
                eligible = false;
                missing.add("CGPA below required minimum of " + drive.getMinCgpa());
                reasons.add("Academic requirement not met");
            }
        }

        if (student.getBacklogs() != null && drive.getMaxBacklogs() != null) {
            if (student.getBacklogs() > drive.getMaxBacklogs()) {
                eligible = false;
                missing.add("Active backlogs (" + student.getBacklogs() + ") exceed maximum allowed (" + drive.getMaxBacklogs() + ")");
                reasons.add("Clear backlogs to become eligible");
            }
        }

        result.setEligible(eligible);
        result.setMatchPercentage(eligible ? 100.0 : 0.0);
        result.setMissingRequirements(missing);
        result.setReasons(reasons);
        return result;
    }
}
