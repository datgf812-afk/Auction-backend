package auction.backend.Mapper;

import auction.backend.DTO.BidRequestDTO;
import auction.backend.DTO.BidResponseDTO;
import auction.backend.entity.Bids;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface Bidmapper {
    BidResponseDTO toResponseDTO(Bids bids);
    Bids toEntity(BidRequestDTO requestDTO);
}
