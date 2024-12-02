-- Xóa cơ sở dữ liệu cũ nếu có
DROP DATABASE IF EXISTS chat_system;

-- Tạo cơ sở dữ liệu mới
CREATE DATABASE chat_system;
USE chat_system;

-- Bảng lưu thông tin người dùng
CREATE TABLE Users (
    UserID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã người dùng
    Username VARCHAR(50) UNIQUE NOT NULL,  -- Tên đăng nhập
    Password VARCHAR(50) NOT NULL,  -- Mật khẩu đã mã hóa
    FullName VARCHAR(50),  -- Tên đầy đủ
    Address VARCHAR(100),
    DateOfBirth DATE DEFAULT '2000-01-01',
    Email varchar(50),
    Phone varchar(10),
    Gender ENUM('Male', 'Female', 'Other'),
    Status ENUM('online', 'offline') DEFAULT 'offline',  -- Trạng thái người dùng
    Access ENUM('yes', 'no') DEFAULT 'yes', -- Quyền truy cập vào tài khoản (cho chức năng khóa tài khoản)
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Thời gian tạo tài khoản
);

-- Bảng lưu thông tin quản trị viên
CREATE TABLE Administrators (
    AdminID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã quản trị viên
    UserID INT NOT NULL,  -- Liên kết với người dùng
    AssignedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian phân công
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng lưu danh sách người dùng bị chặn
CREATE TABLE BlockList (
    BlockID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã chặn
    BlockerID INT NOT NULL,  -- Người chặn
    BlockedID INT NOT NULL,  -- Người bị chặn
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian chặn
    FOREIGN KEY (BlockerID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (BlockedID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng danh sách bạn bè và trạng thái
CREATE TABLE Friends (
    ID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã bạn bè
    UserID INT NOT NULL,  -- ID người dùng
    FriendUserID INT NOT NULL,  -- ID bạn bè
    IsBlocked BOOLEAN DEFAULT FALSE,  -- Trạng thái bị chặn
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian kết bạn
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (FriendUserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng lưu các yêu cầu kết bạn
CREATE TABLE FriendRequests (
    RequestID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã yêu cầu
    SenderID INT NOT NULL,  -- Người gửi yêu cầu
    ReceiverID INT NOT NULL,  -- Người nhận yêu cầu
    Status ENUM('pending', 'accepted', 'declined') DEFAULT 'pending',  -- Trạng thái
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi yêu cầu
    FOREIGN KEY (SenderID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (ReceiverID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng lưu tin nhắn cá nhân
CREATE TABLE Messages (
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
CREATE TABLE GroupInfo (
    GroupID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã nhóm
    GroupName VARCHAR(100) NOT NULL,  -- Tên nhóm
    AdminID INT NOT NULL,  -- Quản trị viên nhóm
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian tạo nhóm
    FOREIGN KEY (AdminID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng thành viên nhóm
CREATE TABLE GroupMembers (
    MemberID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã thành viên
    GroupID INT NOT NULL,  -- Mã nhóm
    UserID INT NOT NULL,  -- Mã người dùng
    IsAdmin BOOLEAN DEFAULT FALSE,  -- Quản trị viên nhóm
    AddedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian tham gia nhóm
    FOREIGN KEY (GroupID) REFERENCES GroupInfo(GroupID) ON DELETE CASCADE,
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng tin nhắn nhóm
CREATE TABLE GroupMessages (
    MessageID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã tin nhắn nhóm
    GroupID INT NOT NULL,  -- Mã nhóm
    SenderID INT NOT NULL,  -- Người gửi
    Content TEXT NOT NULL,  -- Nội dung tin nhắn
    SentAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi
    FOREIGN KEY (GroupID) REFERENCES GroupInfo(GroupID) ON DELETE CASCADE,
    FOREIGN KEY (SenderID) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng báo cáo spam
CREATE TABLE SpamReports (
    ReportID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã báo cáo
    UserID INT NOT NULL,  -- Mã người bị báo cáo
    ReportedBy INT NOT NULL,  -- Mã người báo cáo
    ReportTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian báo cáo
    Reason TEXT NOT NULL,  -- Lý do báo cáo
    FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE CASCADE,
    FOREIGN KEY (ReportedBy) REFERENCES Users(UserID) ON DELETE CASCADE
);

-- Bảng hoạt động của người dùng
CREATE TABLE UserActivities (
    ActivityID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã hoạt động
    UserID INT NOT NULL,  -- Mã người dùng
    ActivityType VARCHAR(50),  -- Loại hoạt động (Login, Message, ...)
    LoginTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian đăng nhập
    LogoutTime TIMESTAMP NULL,  -- Thời gian đăng xuất (nếu có)
    FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

-- Bảng lịch sử trò chuyện (bao gồm tin nhắn cá nhân và nhóm)
CREATE TABLE ChatHistory (
    ChatHistoryID INT AUTO_INCREMENT PRIMARY KEY,  -- Mã lịch sử trò chuyện
    UserID INT NOT NULL,  -- Mã người dùng
    GroupID INT DEFAULT NULL,  -- Mã nhóm (nếu có)
    MessageContent TEXT NOT NULL,  -- Nội dung tin nhắn
    MessageTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Thời gian gửi tin nhắn
    IsGroupMessage BOOLEAN DEFAULT FALSE,  -- Tin nhắn nhóm hay cá nhân
    FOREIGN KEY (UserID) REFERENCES Users(UserID),
    FOREIGN KEY (GroupID) REFERENCES GroupInfo(GroupID)
);
