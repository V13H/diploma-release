package com.hvs.diploma.dao.sms;

import com.hvs.diploma.turbo_sms.TurboSmsMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurboSmsMessageRepository extends JpaRepository<TurboSmsMessage, Integer> {
    TurboSmsMessage findTopByOrderByIdDesc();
}
