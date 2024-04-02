import {ColumnDef} from "@tanstack/react-table";
import {Button} from "@/components/ui/button/button.tsx";
import {CaretSortIcon} from "@radix-ui/react-icons";
import {ToastAction} from "@radix-ui/react-toast";
import {toast} from "@/components/ui/use-toast.ts";
import {Copy, KeyRound} from "lucide-react";
import {Dispatch, SetStateAction} from "react";
import {EncryptedMessage} from "@/dto/CypherMessage";
import {
    HoverCard,
    HoverCardContent,
    HoverCardTrigger,
} from "@/components/ui/hover-card"

interface columnsWrapperProps {
    setOpenDialog: Dispatch<SetStateAction<boolean>>
    setMessageId: Dispatch<SetStateAction<string>>
}

export function columnsWrapper({
                                   setOpenDialog,
                                   setMessageId
                               }: columnsWrapperProps): ColumnDef<EncryptedMessage>[] {
    return ([
            {
                header: 'ID',
                accessorKey: 'id',
                //how only first 10char
                cell: ({cell}) => {
                    const value = cell.getValue() as string;
                    return (
                        <div className="whitespace-nowrap overflow-ellipsis overflow-hidden">
                            {value.slice(0, 5).padEnd(10, "*")}
                        </div>
                    )
                }
            },
            {
                header: ({column}) => {
                    return (
                        <Button
                            size="sm"
                            className="p-0 "
                            variant="ghost"
                            onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
                        >
                            Created Date
                            <CaretSortIcon className="ml-1 h-4 w-4"/>
                        </Button>
                    );
                },
                accessorKey: 'createdDateTime',
                cell: ({cell}) => {
                    const value = cell.getValue() as string;
                    const date = new Date(value)
                    const formattedDate = [
                        String(date.getDate()).padStart(2, '0'),
                        String(date.getMonth() + 1).padStart(2, '0'),
                        date.getFullYear(),
                    ].join('.');

                    return (
                        <div className="whitespace-nowrap overflow-ellipsis overflow-hidden m-0 p-0">
                            {formattedDate}
                        </div>
                    )
                }
            },
            {
                header: 'Encrypted Message',
                accessorKey: 'encryptedMessage',
                cell: ({cell}) => {
                    const value = cell.getValue() as string;
                    return (
                        <Button className="flex justify-around gap-3"
                                variant="ghost"

                                onClick={() => {
                                    navigator.clipboard.writeText(value)
                                    toast({
                                        variant: "default",
                                        title: "Copied to clipboard",
                                        action: <ToastAction altText="Close">Close</ToastAction>,
                                    });
                                }}
                        >
                            {/*show first 5 char and 5 as **/}
                            {value.slice(0, 5)}{value.slice(5, 10).replace(/./g, "*")}
                            <Copy size={16} strokeWidth={1.25}/>
                        </Button>
                    )

                }
            },
            {
                accessorKey: 'messageIdentifier',
                header: ({column}) => {
                    return (
                        <Button
                            className="px-0"
                            variant="ghost"
                            onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
                        >
                            Message ID
                            <CaretSortIcon className="ml-1 h-4 w-4"/>
                        </Button>
                    );
                },
                cell: ({row}) => {
                    const messageIdentifier = row.getValue("messageIdentifier") as string;
                    const isTruncated = messageIdentifier.length > 10;
                    const displayText = isTruncated ? `${messageIdentifier.slice(0, 10)}...` : messageIdentifier;

                    return (
                        <div>
                            {isTruncated ? (
                                <HoverCard>
                                    <HoverCardTrigger asChild>
                                        <button>{displayText}</button>
                                    </HoverCardTrigger>
                                    <HoverCardContent className='w-80 overflow-auto'>
                                        <div className='flex justify-between space-x-4'>
                                            <div className='space-y-1'>
                                                <h4 className='text-sm font-semibold'>Description</h4>
                                                <p className='text-sm break-words max-w-80'>
                                                    {messageIdentifier}
                                                </p>
                                            </div>
                                        </div>
                                    </HoverCardContent>
                                </HoverCard>
                            ) : (
                                <div>{displayText}</div>
                            )}
                        </div>
                    );
                }
            },
            {
                header: ({column}) => {
                    return (
                        <Button
                            className="p-0 flex"
                            variant="ghost"
                            onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
                        >
                            Was Decrypted
                            <CaretSortIcon className="ml-1 h-4 w-4"/>
                        </Button>
                    );
                },
                accessorKey: 'decrypted'
            },
            {
                header: 'Actions',
                cell: ({row}) => {
                    const message = row.original; // Assuming 'id' is the correct identifier
                    return (
                        <Button
                            variant="outline"
                            size="icon"
                            onClick={() => {
                                setOpenDialog(true)
                                setMessageId(message.id)
                            }}
                        >
                            <KeyRound size={20} strokeWidth={1}/>
                        </Button>
                    )
                }
            }
        ]
    )
}
