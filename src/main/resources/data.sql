

set @priceSizeSmall = (select count(psp.id) from park_ticketing.parking_slot_price psp where psp.size ='Small');
set @priceSizeMedium = (select count(psp.id) from park_ticketing.parking_slot_price psp where psp.size ='Medium');
set @priceSizeLarge = (select count(psp.id) from park_ticketing.parking_slot_price psp where psp.size ='Large');

INSERT INTO park_ticketing.parking_slot_price (`created_by`,`date_created`,`hour`,`price`,`size`,`type`)
select prices.* from (
(select 'SYSTEM',now(),3,40.00,'Small','Flat Rate' where 0 = @priceSizeSmall)
union (select 'SYSTEM',now(),1,20.00,'Small','Per Hour' where 0 = @priceSizeSmall)
union (select 'SYSTEM',now(),24,5000.00,'Small','Day Rate' where 0 = @priceSizeSmall)
union (select 'SYSTEM',now(),3,40.00,'Medium','Flat Rate' where 0 = @priceSizeMedium)
union (select 'SYSTEM',now(),1,60.00,'Medium','Per Hour' where 0 = @priceSizeMedium)
union (select 'SYSTEM',now(),24,5000.00,'Medium','Day Rate' where 0 = @priceSizeMedium)
union (select 'SYSTEM',now(),3,40.00,'Large','Flat Rate' where 0 = @priceSizeLarge)
union (select 'SYSTEM',now(),1,100.00,'Large','Per Hour' where 0 = @priceSizeLarge)
union (select 'SYSTEM',now(),24,5000.00,'Large','Day Rate' where 0 = @priceSizeLarge)) prices;

insert into park_ticketing.parking_lot(`created_by`, `name`)
select 'SYSTEM', 'Test Mall Park Lot'  where not exists (select id from park_ticketing.parking_lot where name = 'Test Mall Park Lot');

set @testParkingLotId = (select id from park_ticketing.parking_lot where name = 'Test Mall Park Lot');
set @testEntranceSize = (select count(id) from park_ticketing.parking_entrance where parking_lot_id = @testParkingLotId);
set @testParkingSlotSize = (select count(id) from park_ticketing.parking_slot where parking_lot_id = @testParkingLotId);
select @testEntranceSize;
insert into park_ticketing.parking_entrance(`created_by`, `name`, `parking_lot_id`)
select e.* from ((select 'SYSTEM', 'Entrance A', @testParkingLotId)
union (select 'SYSTEM', 'Entrance B', @testParkingLotId)
union (select 'SYSTEM', 'Entrance C', @testParkingLotId)) e where (@testEntranceSize = 0  or @testEntranceSize is null) and @testParkingLotId is not null;

insert into park_ticketing.parking_slot(`created_by`, `name`, `size`, `parking_lot_id`)
select s.* from ((select 'SYSTEM', 'Slot 1', 'Small', @testParkingLotId)
union (select 'SYSTEM', 'Slot 2', 'Small', @testParkingLotId)
union (select 'SYSTEM', 'Slot 3', 'Small', @testParkingLotId)
union (select 'SYSTEM', 'Slot 4', 'Medium', @testParkingLotId)
union (select 'SYSTEM', 'Slot 5', 'Medium', @testParkingLotId)
union (select 'SYSTEM', 'Slot 6', 'Medium', @testParkingLotId)
union (select 'SYSTEM', 'Slot 7', 'Large', @testParkingLotId)
union (select 'SYSTEM', 'Slot 8', 'Large', @testParkingLotId)
union (select 'SYSTEM', 'Slot 9', 'Large', @testParkingLotId)) s where (@testParkingSlotSize = 0 or @testParkingSlotSize is null) and @testParkingLotId is not null;

insert into park_ticketing.parking_entrance_slot(`distance`, `parking_entrance_id`, `parking_slot_id`)
select (FLOOR(RAND()*(10-5+1)+5)) as `distance`, pe.id, ps.id
from park_ticketing.parking_lot pl
inner join park_ticketing.parking_entrance pe
on pe.parking_lot_id = pl.id
inner join park_ticketing.parking_slot ps
on ps.parking_lot_id = pl.id
where not exists (select id from park_ticketing.parking_entrance_slot where parking_entrance_id = pe.id and parking_slot_id = ps.id)
