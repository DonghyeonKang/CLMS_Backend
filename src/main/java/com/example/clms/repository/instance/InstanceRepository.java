package com.example.clms.repository.instance;

import com.example.clms.entity.instance.Instance;

import java.util.List;
import java.util.Optional;

public interface InstanceRepository {
    Optional<Integer> findIdByUserIdAndLectureId(Long userid, Long lectureId);

    Instance save(Instance instance);

    Optional<Instance> findById(int instanceId);

    Instance getReferenceById(int instanceId);
    List<Instance> findAllByLectureId(Long lectureId);
    void deleteById(int instanceId);
}
