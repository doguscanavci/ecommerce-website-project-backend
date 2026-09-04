package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.request.LoginRequest;
import com.bandage.ecommerce.dto.request.SignUpRequest;
import com.bandage.ecommerce.dto.response.AuthResponse;
import com.bandage.ecommerce.entity.Role;
import com.bandage.ecommerce.entity.Store;
import com.bandage.ecommerce.entity.User;
import com.bandage.ecommerce.exception.ApiException;
import com.bandage.ecommerce.repository.RoleRepository;
import com.bandage.ecommerce.repository.UserRepository;
import com.bandage.ecommerce.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public void signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException("Bu email adresi zaten kullanılıyor", HttpStatus.CONFLICT);
        }

        Role role = roleRepository.findById(request.getRole_id())
                .orElseThrow(() -> new ApiException("Geçersiz role_id", HttpStatus.BAD_REQUEST));

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        // If signing up as a store, attach store info
        if ("store".equalsIgnoreCase(role.getCode())) {
            if (request.getStore() == null) {
                throw new ApiException("Mağaza rolü için store bilgileri zorunludur", HttpStatus.BAD_REQUEST);
            }
            Store store = Store.builder()
                    .name(request.getStore().getName())
                    .phone(request.getStore().getPhone())
                    .taxNo(request.getStore().getTax_no())
                    .bankAccount(request.getStore().getBank_account())
                    .user(user)
                    .build();
            user.setStore(store);
        }

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("Email veya şifre hatalı", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("Email veya şifre hatalı", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .role_id(user.getRole().getId())
                .build();
    }

    public AuthResponse verify(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("Kullanıcı bulunamadı", HttpStatus.UNAUTHORIZED));

        // Issue a fresh token (sliding expiration on each successful verify)
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .name(user.getName())
                .email(user.getEmail())
                .role_id(user.getRole().getId())
                .build();
    }
}