-- Xóa cơ sở dữ liệu cũ nếu có
DROP DATABASE IF EXISTS chat_system;

-- Tạo cơ sở dữ liệu mới
CREATE DATABASE chat_system;
USE chat_system;

-- Bảng lưu thông tin người dùng
CREATE TABLE users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã người dùng
    Username VARCHAR(50) UNIQUE NOT NULL,  -- Tên đăng nhập
    Password VARCHAR(50) NOT NULL,  -- Mật khẩu đã mã hóa
    FullName VARCHAR(50),  -- Tên đầy đủ
    Address VARCHAR(100),
    DateOfBirth DATE DEFAULT '2000-01-01',
    Email varchar(50),
    Phone varchar(10),
    Gender ENUM('Male', 'Female', 'Other'),
    Status ENUM('Online', 'Offline') DEFAULT 'Offline',  -- Trạng thái người dùng
    Access ENUM('Yes', 'No') DEFAULT 'Yes', -- Quyền truy cập vào tài khoản (cho chức năng khóa tài khoản)
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Thời gian tạo tài khoản
);

INSERT INTO users (Username, Password, FullName, Address, DateOfBirth, Email, Phone, Gender)
VALUES
('e1', '123', 'Nguyễn Văn A', 'Hà Nội, Việt Nam', '1990-01-01', 'nguyenvana@example.com', '0123456789', 'Male'),
('e2', '123', 'Trần Thị B', 'TP.HCM, Việt Nam', '1992-02-02', 'tranthib@example.com', '0987654321', 'Female'),
('e3', '123', 'Lê Minh C', 'Đà Nẵng, Việt Nam', '1985-03-03', 'leminhc@example.com', '0912345678', 'Male'),
('e4', '123', 'Phạm Thị D', 'Hải Phòng, Việt Nam', '1995-04-04', 'phamthid@example.com', '0945123456', 'Female'),
('e5', '123', 'Nguyễn Minh E', 'Cần Thơ, Việt Nam', '2000-05-05', 'nguyenmine@example.com', '0976123456', 'Male'),
('e6', '123', 'Lê Thị F', 'Bình Dương, Việt Nam', '1994-06-06', 'lethif@example.com', '0986123456', 'Female'),
('e7', '123', 'Trần Minh G', 'Vũng Tàu, Việt Nam', '1988-07-07', 'tranming@example.com', '0903123456', 'Male'),
('e8', '123', 'Phạm Thị H', 'Quảng Ninh, Việt Nam', '1996-08-08', 'phamthih@example.com', '0965123456', 'Female'),
('e9', '123', 'Nguyễn Minh I', 'Nam Định, Việt Nam', '1992-09-09', 'nguyenmini@example.com', '0916123456', 'Male'),
('e10', '123', 'Lê Thị J', 'Hà Tĩnh, Việt Nam', '2001-10-10', 'lethij@example.com', '0932123456', 'Female');


-- Bảng lưu thông tin quản trị viên
CREATE TABLE administrators (
    AdminID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã quản trị viên
    UserID INT NOT NULL,  -- Liên kết với người dùng
    AssignedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian phân công
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng lưu danh sách người dùng bị chặn
CREATE TABLE block_list (
    BlockID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã chặn
    BlockerID INT NOT NULL,  -- Người chặn
    BlockedID INT NOT NULL,  -- Người bị chặn
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian chặn
    FOREIGN KEY (BlockerID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (BlockedID) REFERENCES Users(UserID) ON DELETE CASCADE
);
-- INSERT INTO block_list (BlockerID, BlockedID)
-- VALUES
-- (2, 1), (3, 1);

-- Bảng danh sách bạn bè và trạng thái
CREATE TABLE friends (
    FriendshipID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã bạn bè
    User1ID INT NOT NULL,  -- ID người dùng 1
    User2ID INT NOT NULL,  -- ID người dùng 2
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian kết bạn
    FOREIGN KEY (User1ID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (User2ID) REFERENCES Users(UserID) ON DELETE CASCADE
);

INSERT INTO friends (User1ID, User2ID)
VALUES
(1,2),(1,3),(1,7),(1,8),
(2,3),(2,5),
(3,4),(3,5),(3,6),
(4,5),
(5,6),
(6,4),
(7,4),(7,5),
(8,4),(8,6),
(9,4),(9,5),
(10,4),(10,6);


-- Bảng lưu các yêu cầu kết bạn
CREATE TABLE friend_requests (
    RequestID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã yêu cầu
    SenderID INT NOT NULL,  -- Người gửi yêu cầu
    ReceiverID INT NOT NULL,  -- Người nhận yêu cầu
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi yêu cầu
    FOREIGN KEY (SenderID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (ReceiverID) REFERENCES Users(UserID) ON DELETE CASCADE
);

INSERT INTO friend_requests (SenderID, ReceiverID)
VALUES
(8, 2), (2, 10);

-- Bảng lưu tin nhắn cá nhân
CREATE TABLE messages (
    MessageID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã tin nhắn
    SenderID INT NOT NULL,  -- Người gửi
    ReceiverID INT NOT NULL,  -- Người nhận
    Content TEXT NOT NULL,  -- Nội dung tin nhắn
    SentAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi
    FOREIGN KEY (SenderID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (ReceiverID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng các cuộc trò chuyện
CREATE TABLE conversations (
    ConversationID INT AUTO_INCREMENT PRIMARY KEY, -- Mã cuộc hội thoại
    User1ID INT NOT NULL,                           -- Mã người dùng
    User2ID INT NOT NULL,                         -- Mã bạn bè
    LastMessage TEXT,                              -- Tin nhắn gần nhất
    LastMessageTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    -- Thời gian của tin nhắn gần nhất
    FOREIGN KEY (User1ID) REFERENCES users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (User2ID) REFERENCES users(UserID) ON DELETE CASCADE,
    UNIQUE(User1ID, User2ID)                       -- Đảm bảo mỗi cặp UserID-FriendID chỉ có một cuộc hội thoại
);

CREATE TABLE group_conversations (
    ConversationID INT AUTO_INCREMENT PRIMARY KEY,   -- Mã cuộc hội thoại
    GroupID INT NOT NULL,                            -- Mã nhóm
    LastMessage TEXT NOT NULL,                       -- Tin nhắn gần nhất
    LastMessageTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian của tin nhắn gần nhất
    SenderID INT NOT NULL,                           -- Người gửi tin nhắn
    FOREIGN KEY (GroupID) REFERENCES group_info(GroupID) ON DELETE CASCADE,  -- Liên kết tới bảng nhóm
    FOREIGN KEY (SenderID) REFERENCES Users(UserID) ON DELETE CASCADE   -- Liên kết tới bảng người dùng
);


-- Bảng thông tin nhóm
CREATE TABLE group_info (
    GroupID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã nhóm
    GroupName VARCHAR(100) NOT NULL,  -- Tên nhóm
    AdminID INT NOT NULL,  -- Quản trị viên nhóm
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian tạo nhóm
    FOREIGN KEY (AdminID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng thành viên nhóm
CREATE TABLE group_members (
    GroupID INT NOT NULL,  -- Mã nhóm
    UserID INT NOT NULL,  -- Mã người dùng
    AddedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian tham gia nhóm
    PRIMARY KEY (GroupID, UserID),
    FOREIGN KEY (GroupID) REFERENCES group_info(GroupID) ON DELETE CASCADE,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng tin nhắn nhóm
CREATE TABLE group_messages (
    MessageID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã tin nhắn nhóm
    GroupID INT NOT NULL,  -- Mã nhóm
    SenderID INT NOT NULL,  -- Người gửi
    Content TEXT NOT NULL,  -- Nội dung tin nhắn
    SentAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi
    FOREIGN KEY (GroupID) REFERENCES group_info(GroupID) ON DELETE CASCADE,
    FOREIGN KEY (SenderID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng báo cáo spam
CREATE TABLE spam_reports (
    ReportID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã báo cáo
    UserID INT NOT NULL,  -- Mã người bị báo cáo
    ReportedBy INT NOT NULL,  -- Mã người báo cáo
    ReportTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian báo cáo
    Reason TEXT NOT NULL,  -- Lý do báo cáo
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (ReportedBy) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng hoạt động của người dùng
CREATE TABLE user_activities (
    ActivityID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã hoạt động
    UserID INT NOT NULL,  -- Mã người dùng
    LoginTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian đăng nhập
    LogoutTime TIMESTAMP NULL,  -- Thời gian đăng xuất (nếu có)
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Bảng lịch sử trò chuyện (bao gồm tin nhắn cá nhân và nhóm)
CREATE TABLE chat_history (
    ChatHistoryID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã lịch sử trò chuyện
    UserID INT NOT NULL,  -- Mã người dùng
    GroupID INT DEFAULT NULL,  -- Mã nhóm (nếu có)
    MessageContent TEXT NOT NULL,  -- Nội dung tin nhắn
    MessageTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi tin nhắn
    IsGroupMessage BOOLEAN DEFAULT FALSE,  -- Tin nhắn nhóm hay cá nhân
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (GroupID) REFERENCES group_info(GroupID)
);

-- Tạo dữ liệu giả cho bảng administrators
INSERT INTO administrators (UserID) VALUES
(1), (2);

-- Tạo dữ liệu giả cho bảng group_info
INSERT INTO group_info (GroupName, AdminID) VALUES
('Nhóm IT', 1),
('Nhóm Sách', 2),
('Nhóm Du Lịch', 3);

-- Tạo dữ liệu giả cho bảng group_members
INSERT INTO group_members (GroupID, UserID) VALUES
(1, 1), (1, 2), (1, 3), (1, 4),
(2, 2), (2, 5), (2, 6),
(3, 3), (3, 7), (3, 8), (3, 9);

-- Tạo dữ liệu giả cho bảng group_messages
INSERT INTO group_messages (GroupID, SenderID, Content) VALUES
(1, 1, 'Chào mừng mọi người đến với nhóm IT!'),
(1, 2, 'Cảm ơn admin!'),
(2, 2, 'Ai có sách nào hay giới thiệu nhé!'),
(2, 5, 'Tôi mới đọc xong một quyển rất hay.'),
(3, 3, 'Chuyến du lịch tháng tới mọi người sắp xếp nhé.');

-- Tạo dữ liệu giả cho bảng spam_reports
INSERT INTO spam_reports (UserID, ReportedBy, Reason) VALUES
(1, 3, 'Spam tin nhắn không cần thiết.'),
(2, 4, 'Quảng cáo liên tục trong nhóm.');

-- Tạo dữ liệu giả cho bảng user_activities
INSERT INTO user_activities (UserID, LoginTime, LogoutTime) VALUES
(1, '2024-12-06 08:00:00', '2024-12-06 10:00:00'),
(2, '2024-12-06 09:00:00', NULL),
(3, '2024-12-06 08:30:00', '2024-12-06 09:30:00'),
(4, '2024-12-06 10:00:00', NULL);

-- Tạo dữ liệu giả cho bảng chat_history
INSERT INTO chat_history (UserID, GroupID, MessageContent, IsGroupMessage) VALUES
(1, NULL, 'Hello! Bạn đang làm gì vậy?', FALSE),
(2, 1, 'Mọi người đã chuẩn bị bài tập nhóm chưa?', TRUE),
(3, 2, 'Tôi đang tìm hiểu sách cho chủ đề tuần này.', TRUE),
(4, 3, 'Hẹn gặp mọi người ở điểm tập trung nhé.', TRUE);
