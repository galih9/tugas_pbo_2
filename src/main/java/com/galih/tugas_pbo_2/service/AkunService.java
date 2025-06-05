
package com.galih.tugas_pbo_2.service;

import com.galih.tugas_pbo_2.model.Akun;

public interface AkunService {
    Akun login(String email, String password);
    boolean register(Akun akun);
}