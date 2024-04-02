export interface EncryptedMessage {
    id: string;
    createdDateTime: Date;
    encryptedMessage: string;
    messageIdentifier: string;
    decrypted: boolean;
}

export interface ToEncryptMessage {
    message: string;
    password: string;
    messageIdentifier: string;
}

export interface DecryptedMessageResponse {
    id: string;
    createdDateTime: Date;
    message: string;
    messageIdentifier: string;
    decrypted: boolean;
}