package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.request.AddressRequest;
import com.bandage.ecommerce.dto.response.AddressResponse;
import com.bandage.ecommerce.entity.Address;
import com.bandage.ecommerce.entity.User;
import com.bandage.ecommerce.exception.ApiException;
import com.bandage.ecommerce.repository.AddressRepository;
import com.bandage.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public List<AddressResponse> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(AddressResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public AddressResponse createAddress(Long userId, AddressRequest request) {
        User user = userRepository.getReferenceById(userId);

        Address address = Address.builder()
                .title(request.getTitle())
                .name(request.getName())
                .surname(request.getSurname())
                .phone(request.getPhone())
                .city(request.getCity())
                .district(request.getDistrict())
                .neighborhood(request.getNeighborhood())
                .address(request.getAddress())
                .user(user)
                .build();

        return AddressResponse.from(addressRepository.save(address));
    }

    @Transactional
    public AddressResponse updateAddress(Long userId, AddressRequest request) {
        if (request.getId() == null) {
            throw new ApiException("Güncelleme için id zorunludur", HttpStatus.BAD_REQUEST);
        }

        Address address = addressRepository.findById(request.getId())
                .orElseThrow(() -> new ApiException("Adres bulunamadı", HttpStatus.NOT_FOUND));

        if (!address.getUser().getId().equals(userId)) {
            throw new ApiException("Bu adres size ait değil", HttpStatus.FORBIDDEN);
        }

        address.setTitle(request.getTitle());
        address.setName(request.getName());
        address.setSurname(request.getSurname());
        address.setPhone(request.getPhone());
        address.setCity(request.getCity());
        address.setDistrict(request.getDistrict());
        address.setNeighborhood(request.getNeighborhood());
        address.setAddress(request.getAddress());

        return AddressResponse.from(addressRepository.save(address));
    }

    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        if (!addressRepository.existsByIdAndUserId(addressId, userId)) {
            throw new ApiException("Adres bulunamadı veya size ait değil", HttpStatus.NOT_FOUND);
        }
        addressRepository.deleteById(addressId);
    }
}
