package thunderbirdsonly.nwhackbackend.DOT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    private Long sessionId;
    private Long userId;
    private Integer focus;
    private Integer unfocus;
}
