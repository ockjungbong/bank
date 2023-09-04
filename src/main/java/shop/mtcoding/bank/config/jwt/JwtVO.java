package shop.mtcoding.bank.config.jwt;

/*
 * SECRET은 노출되면 안된다.(DB, 클라우드에 환경변수로 저장)
 * 원래 리프레시 토큰도 구현해야 하는데 본 강의에서는 다루지 않는다.
 */
public interface JwtVO {

  public static final String SECRET = "메타코딩"; // HS256(대칭키)
  public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 7일 (1/1000초)
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER = "Authorization";

}
