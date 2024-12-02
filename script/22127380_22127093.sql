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
('u1', '123', 'Nguyễn Văn A', 'Hà Nội, Việt Nam', '1990-01-01', 'nguyenvana@example.com', '0123456789', 'Male'),
('u2', '123', 'Trần Thị B', 'TP.HCM, Việt Nam', '1992-02-02', 'tranthib@example.com', '0987654321', 'Female'),
('u3', '123', 'Lê Minh C', 'Đà Nẵng, Việt Nam', '1985-03-03', 'leminhc@example.com', '0912345678', 'Male'),
('u4', '123', 'Phạm Thị D', 'Hải Phòng, Việt Nam', '1995-04-04', 'phamthid@example.com', '0945123456', 'Female'),
('u5', '123', 'Nguyễn Minh E', 'Cần Thơ, Việt Nam', '2000-05-05', 'nguyenmine@example.com', '0976123456', 'Male'),
('u6', '123', 'Lê Thị F', 'Bình Dương, Việt Nam', '1994-06-06', 'lethif@example.com', '0986123456', 'Female'),
('u7', '123', 'Trần Minh G', 'Vũng Tàu, Việt Nam', '1988-07-07', 'tranming@example.com', '0903123456', 'Male'),
('u8', '123', 'Phạm Thị H', 'Quảng Ninh, Việt Nam', '1996-08-08', 'phamthih@example.com', '0965123456', 'Female'),
('u9', '123', 'Nguyễn Minh I', 'Nam Định, Việt Nam', '1992-09-09', 'nguyenmini@example.com', '0916123456', 'Male'),
('u10', '123', 'Lê Thị J', 'Hà Tĩnh, Việt Nam', '2001-10-10', 'lethij@example.com', '0932123456', 'Female');


-- Bảng lưu thông tin quản trị viên
CREATE TABLE administrators (
    AdminID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã quản trị viên
    UserID INT NOT NULL,  -- Liên kết với người dùng
    AssignedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian phân công
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng lưu danh sách người dùng bị chặn
CREATE TABLE blockList (
    BlockID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã chặn
    BlockerID INT NOT NULL,  -- Người chặn
    BlockedID INT NOT NULL,  -- Người bị chặn
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian chặn
    FOREIGN KEY (BlockerID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (BlockedID) REFERENCES Users(UserID) ON DELETE CASCADE
);

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
(1, 2), (1, 3), (1, 4), (1, 5), (1, 7), (1, 8), (2, 3), (2, 4), (2, 5), (2, 7),
(2, 8), (2, 9), (2, 10), (3, 1), (3, 2), (3, 4), (3, 5), (3, 6), (3, 9);


-- Bảng lưu các yêu cầu kết bạn
CREATE TABLE friend_requests (
    RequestID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã yêu cầu
    SenderID INT NOT NULL,  -- Người gửi yêu cầu
    ReceiverID INT NOT NULL,  -- Người nhận yêu cầu
    Status ENUM('Pending', 'Accepted', 'Declined') DEFAULT 'Pending',  -- Trạng thái
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi yêu cầu
    FOREIGN KEY (SenderID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (ReceiverID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng lưu tin nhắn cá nhân
CREATE TABLE messages (
    MessageID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã tin nhắn
    SenderID INT NOT NULL,  -- Người gửi
    ReceiverID INT NOT NULL,  -- Người nhận
    Content TEXT NOT NULL,  -- Nội dung tin nhắn
    SentAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi
    IsRead BOOLEAN DEFAULT FALSE,  -- Trạng thái đã đọc
    FOREIGN KEY (SenderID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (ReceiverID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng thông tin nhóm
CREATE TABLE groupInfo (
    GroupID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã nhóm
    GroupName VARCHAR(100) NOT NULL,  -- Tên nhóm
    AdminID INT NOT NULL,  -- Quản trị viên nhóm
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian tạo nhóm
    FOREIGN KEY (AdminID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng thành viên nhóm
CREATE TABLE groupMembers (
    MemberID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã thành viên
    GroupID INT NOT NULL,  -- Mã nhóm
    UserID INT NOT NULL,  -- Mã người dùng
    IsAdmin BOOLEAN DEFAULT FALSE,  -- Quản trị viên nhóm
    AddedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian tham gia nhóm
    FOREIGN KEY (GroupID) REFERENCES GroupInfo(GroupID) ON DELETE CASCADE,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng tin nhắn nhóm
CREATE TABLE group_messages (
    MessageID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã tin nhắn nhóm
    GroupID INT NOT NULL,  -- Mã nhóm
    SenderID INT NOT NULL,  -- Người gửi
    Content TEXT NOT NULL,  -- Nội dung tin nhắn
    SentAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi
    FOREIGN KEY (GroupID) REFERENCES GroupInfo(GroupID) ON DELETE CASCADE,
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
    ActivityType VARCHAR(50),  -- Loại hoạt động (Login, Message, ...)
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
    FOREIGN KEY (GroupID) REFERENCES GroupInfo(GroupID)
);
