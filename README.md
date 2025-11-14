# Akanoid – CaiWin Edition

Akanoid là trò chơi phá gạch xây dựng bằng JavaFX mang cảm hứng từ Arkanoid cổ điển nhưng được mở rộng với hiệu ứng hình ảnh, nhạc nền và hàng loạt cơ chế mới. Một đoạn intro dạng video sẽ phát trước khi người chơi được đưa vào menu chính và chuỗi trạng thái GameManager sẽ luân phiên giữa Menu → Chọn màn → Chơi → Thắng/Thua.

## Lối chơi cốt lõi
- Vòng lặp game chạy ở 70 FPS, bảo đảm thao tác paddle mượt và xử lý va chạm chính xác.
- Các màn chơi được vẽ trong khung hình 900x700 nằm giữa cửa sổ 1200x750.
- Hệ thống trạng thái (`GameState`) điều hướng toàn bộ trải nghiệm: LoadingProcess, MenuProcess, PickLevelProcess, PlayingProcess, OptionProcess, TutorialProcess, GameOverProcess.
- HUD hiển thị điểm, số mạng và level hiện tại; khi phá hết gạch, PlayingProcess tự động chuyển sang level kế tiếp.


## Biểu đồ lớp
<img width="4920" height="1990" alt="java" src="https://github.com/user-attachments/assets/d1dfb9c0-474d-4105-b0c1-361754628a17" />

## Paddle
- Có ba kích thước: ngắn, chuẩn, mở rộng; việc đổi kích thước giữ nguyên tâm paddle để hạn chế cảm giác "giật".
- Sở hữu số mạng riêng; mất hết mạng sẽ chuyển sang Game Over.
- Có thể bắn đạn khi nhặt Shot power-up, đạn tương tác trực tiếp với mọi loại gạch.
- Di chuyển bị giới hạn trong vùng bản đồ, tốc độ có thể được thay đổi bởi power-up.

## Bóng
- Hướng bóng phản xạ theo vị trí chạm vào paddle, tạo cảm giác điều khiển linh hoạt.
- Sau mỗi pha va chạm, tốc độ tăng nhẹ nhưng bị giới hạn để tránh vượt kiểm soát.
- Điều kiện đặc biệt (góc 0°/90°) được hiệu chỉnh để tránh vòng lặp vô tận.
- Rơi khỏi đáy màn sẽ gọi `onBallLost`; nếu còn bóng phụ, hệ thống chuyển sang bóng tiếp theo.
- Ball collision:
<img width="599" height="261" alt="image" src="https://github.com/user-attachments/assets/b54eebc4-dd33-4cf2-86c8-1910ab20c23c" />

## Hệ thống gạch
| Loại gạch | Đặc điểm |
| --- | --- |
| `NormalBrick` | Phá hủy trong 1 hit. |
| `ImmortalBrick` | Hồi lại điểm bền nếu không bị đánh liên tục; cần nhiều lần bật bóng sát nhau. |
| `LifeUpBrick` | Sinh ra vật phẩm tăng mạng. |
| `GoldBrick` | Rơi vật phẩm vàng cộng điểm lớn. |
| `FallBombBrick` | Drop bom gây mất mạng nếu bắt trúng. |
| `AreaBlastBrick` | Kích hoạt nổ lan, có thể phá hủy nhiều gạch lân cận. |
| `LuckyWheelBrick` | Sinh ngẫu nhiên 1 trong 5 power-up hữu ích. |
| `PushBrick` | Khi phá, toàn bộ hàng gạch phía trên bị đẩy xuống gần paddle. |

- Từ level 7, gạch bắt đầu hạ dần theo thời gian; từ level 10 trở đi có thể sinh thêm hàng mới theo thời gian thực (`SpecialBrickEffect.randomRow`).
- Ở chế độ Ultimate, gạch còn dịch chuyển ngang tạo ra các khoảng trống động.

## Power-up
Power-up rơi tự do với vận tốc cố định (`FALLING_SPEED`) và có 4 trạng thái (FALLING, APPLYING, END, FALLOUT). Thời lượng hiệu lực mặc định ~6 giây.
- `LongerPaddlePowerUp` / `ShortenPaddlePowerUp`
- `BiggerBallPowerUp`
- `DuplicateBallPowerUp`
- `ShotPowerUp`
- `LifeUpPowerUp`
- `GoldPowerUp`, `Gold`
- `FallBoomPowerUp`

Một lượt chơi chỉ cho phép sinh tối đa 20 power-up, trừ các vật phẩm đặc biệt như LifeUp/FallBoom không tính vào giới hạn.

## Cấp độ và chế độ
- 13 màn Classic đầu tiên tăng dần độ khó bằng cách thêm gạch đặc biệt, giảm khoảng cách, và cho phép gạch hạ thấp.
- 2 màn Ultimate mở khóa cơ chế sinh hàng ngẫu nhiên và gạch biết di chuyển.
- Bộ bản đồ cơ sở được tạo từ `map/ListOfMap`, kết hợp dữ liệu trong `staticMap/map.txt` và `immortalMap.txt`, sau đó được `CreatSpecialBrick` gán thêm gạch đặc biệt dựa trên level.

## Điều khiển
| Ngữ cảnh | Phím / Chuột |
| --- | --- |
| Menu & Pick Level | Chuột trái để chọn; phím mũi tên + Enter để di chuyển/nhập. |
| Đang chơi | `LEFT` / `RIGHT` điều khiển paddle, `SPACE` thả bóng ở trạng thái READY, `ESC` tạm dừng hoặc tiếp tục. |
| Game Over | `SPACE` về Menu, `ESC` thoát game. |
| Tutorial | `LEFT` / `RIGHT` hoặc `SPACE` chuyển trang, nút Back trở lại Option. |

## Cài đặt & chạy
Yêu cầu:
- JDK 17 trở lên
- Maven 3.9+

Chạy trực tiếp bằng Maven:

```bash
mvn clean javafx:run
```

Tạo JAR phát hành:

```bash
mvn clean package
java -jar target/akanoid-1.0-SNAPSHOT.jar
```

Nếu chạy ngoài Maven, bảo đảm thêm module JavaFX tương ứng vào `--module-path` (hoặc biến môi trường `PATH_TO_FX`).

## Cấu trúc dự án
- `src/main/java/main/Main.java` – Entry point JavaFX.
- `gamemanager/GameManager.java` – Bộ điều phối trạng thái và vòng lặp chính.
- `process/` – Tập hợp các màn (Loading, Menu, PickLevel, Playing, Option, Tutorial, GameOver).
- `gameobject/` – Lớp cơ sở, bóng, paddle, gạch, power-up.
- `map/` – Sinh map nền, chèn gạch đặc biệt, tiện ích cận cảnh.
- `src/main/resources/` – Ảnh, âm thanh, video intro và các cấu hình map tĩnh.

## Tài nguyên & âm thanh
- `sound/mapsound1.mp3` phát nền vòng lặp; quản lý qua `UI.GlobalSound`.
- `sound/click.wav` dùng cho các nút trong UI.
- Hình ảnh UI và background: `src/main/resources/image`.
- Video intro: `src/main/resources/video/intro.mp4`.

## Tình trạng phát triển
- Một số lớp đang refactor (ví dụ PauseMenu) nên tạm thời chưa được gắn vào PlayingProcess.
- Console có thể xuất các thông báo debug; coi như hành vi bình thường trong giai đoạn phát triển.
- Tài nguyên ảnh kích thước lớn, nên tối ưu nếu chuẩn bị phát hành chính thức.

- Có thể sẽ mở rộng thêm sau này để tạo thành sản phẩm ổn hơn.
## Thành viên
- Nhóm trưởng: Nguyễn Ngọc Phú.
- Thành viên: Ma Đình Kiên, Phạm Tuấn Phong
## Phân công công việc 
<a href="https://docs.google.com/document/d/1OFlPN_8zJcJBUnvsHOD6V-ZPiNAUXaWzxZwewSnSOd8/edit?tab=t.0" target="_blank">Xem tài liệu chi tiết tại Google Docs</a>
## Video demo: 
<a href="[https://docs.google.com/document/d/1OFlPN_8zJcJBUnvsHOD6V-ZPiNAUXaWzxZwewSnSOd8/edit?tab=t.0](https://drive.google.com/file/d/18CLS8GrBo6uLRoVoOXtPZS19hjr8TbDp/view?usp=sharing)" target="_blank">Xem tài liệu chi tiết tại Google Docs</a>

## Hình ảnh demo
- Màn hình load game
<img width="1497" height="973" alt="image" src="https://github.com/user-attachments/assets/86c0f64a-6101-469f-af31-72b546799557" />
- Mở đầu
<img width="1503" height="972" alt="image" src="https://github.com/user-attachments/assets/d51b8988-41df-4a85-865d-d96202b4f29b" />
- Option và Tutorial
<img width="1497" height="977" alt="image" src="https://github.com/user-attachments/assets/895fff7a-e84c-4727-8f63-308d99493744" />
<img width="1496" height="977" alt="image" src="https://github.com/user-attachments/assets/be8610ad-1667-4107-b85b-6cda6eb2d6c2" />
- Pick level - 2 độ khó
<img width="1495" height="976" alt="image" src="https://github.com/user-attachments/assets/7e0078cb-626c-48b4-be51-6ba1432e67a0" />
<img width="1501" height="977" alt="image" src="https://github.com/user-attachments/assets/b3a18981-9762-4b69-8cae-a099086646d1" />
- PauseMenu
<img width="1201" height="788" alt="image" src="https://github.com/user-attachments/assets/037e4d4e-acec-45c0-803b-9e775759e8e5" />
- Gameplay
<img width="1506" height="975" alt="Screenshot 2025-11-12 190859" src="https://github.com/user-attachments/assets/79748044-5e7a-4396-aeb3-c0c831a1e9e8" />
- Gameover
<img width="1506" height="973" alt="image" src="https://github.com/user-attachments/assets/1992b51c-4350-4300-974e-831cda4effd5" />




