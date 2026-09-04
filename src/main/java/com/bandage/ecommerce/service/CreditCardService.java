package com.bandage.ecommerce.service;

import com.bandage.ecommerce.dto.request.CreditCardRequest;
import com.bandage.ecommerce.dto.response.CreditCardResponse;
import com.bandage.ecommerce.entity.CreditCard;
import com.bandage.ecommerce.entity.User;
import com.bandage.ecommerce.exception.ApiException;
import com.bandage.ecommerce.repository.CreditCardRepository;
import com.bandage.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final UserRepository userRepository;

    public List<CreditCardResponse> getUserCards(Long userId) {
        return creditCardRepository.findByUserId(userId).stream()
                .map(CreditCardResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public CreditCardResponse createCard(Long userId, CreditCardRequest request) {
        User user = userRepository.getReferenceById(userId);

        CreditCard card = CreditCard.builder()
                .cardNo(request.getCard_no())
                .expireMonth(request.getExpire_month())
                .expireYear(request.getExpire_year())
                .nameOnCard(request.getName_on_card())
                .user(user)
                .build();

        return CreditCardResponse.from(creditCardRepository.save(card));
    }

    @Transactional
    public CreditCardResponse updateCard(Long userId, CreditCardRequest request) {
        if (request.getId() == null) {
            throw new ApiException("Güncelleme için id zorunludur", HttpStatus.BAD_REQUEST);
        }

        CreditCard card = creditCardRepository.findById(request.getId())
                .orElseThrow(() -> new ApiException("Kart bulunamadı", HttpStatus.NOT_FOUND));

        if (!card.getUser().getId().equals(userId)) {
            throw new ApiException("Bu kart size ait değil", HttpStatus.FORBIDDEN);
        }

        card.setCardNo(request.getCard_no());
        card.setExpireMonth(request.getExpire_month());
        card.setExpireYear(request.getExpire_year());
        card.setNameOnCard(request.getName_on_card());

        return CreditCardResponse.from(creditCardRepository.save(card));
    }

    @Transactional
    public void deleteCard(Long userId, Long cardId) {
        if (!creditCardRepository.existsByIdAndUserId(cardId, userId)) {
            throw new ApiException("Kart bulunamadı veya size ait değil", HttpStatus.NOT_FOUND);
        }
        creditCardRepository.deleteById(cardId);
    }
}
